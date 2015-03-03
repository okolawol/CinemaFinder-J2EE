package Mashup;


import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.ProcessingInstruction;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.SearchParameters;

public class XmlParser {
	
	//This list will hold the element nodes copied
	//from the static xml
	List list;
	
	//This list will hold the element nodes created
	//from comparison with parameters
	ArrayList<Element> dList = new ArrayList<Element>();
	
	//This list will hold the element nodes copied
	//from the static xml
	ArrayList<Element> nList = new ArrayList<Element>();
	
	DAO dataAccess;
	
	//Variable will hold the latitude and longitude
	//of the search parameter
	Point2D.Double search;
	//Radius around the searching point
	Double radius = 0.0;
	
	//constructor
	public XmlParser() throws JDOMException, IOException{
		/*
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("WebContent/xmlFiles/cinemas.xml");
		
		Document document = builder.build(xmlFile);
		Element rootNode = document.getRootElement();
		*/
		dataAccess = new DAO();
		dataAccess.createNodeList();
		dataAccess.photoProcess();
		nList = dataAccess.getNodeList();
		//extracting all children elements from the document
		//list = rootNode.getChildren("cinema");
		/*
		for(int i=0;i<list.size();i++){
			Element node = (Element) list.get(i);
			nList.add(node);
		}
		*/
	}
	
	//This method generates the elements that are within the radius of
	//the search point and also attaches the photos from filckr
	public void populateDlist(Double lati,Double longi, Double radius,String term){
		
		//assigning search point and radius
		this.radius = radius;
		search = new Point2D.Double(lati,longi);
		
		for(int i=0; i<nList.size(); i++){
			
			//getting each element in the array list
			Element node = nList.get(i);
			
			//extracting longitude and latitude values from the
			//element tree
			Double latVal = Double.parseDouble(node.getChildText("latitude"));
			Double longVal = Double.parseDouble(node.getChildText("longitude"));
			Point2D.Double loc = new Point2D.Double(latVal,longVal);
			
			//calculating euclidean distance between
			//search point and element's position
			Double distance = search.distance(loc);
			//if the element's coordinates are within the radius
			if(distance<radius){
				//detach the element from it's former parent
				node.detach();
				//attach flickr photos to it
				//attachPhotos(node,latVal,longVal,term);
				//add it into the dynamic list
				dList.add(node);
			}
		}	
	}
	
	//This method appends the dynamically generated elements
	//to an input empty document
	public void appendXml(Document doc){
		Element cinemas = new Element("cinemas");
		doc.addContent(new ProcessingInstruction("xml-stylesheet","type='text/xsl' href='cinemas.xsl'"));
		doc.setRootElement(cinemas);
		
		//adding the dynamically generated elements into the document
		for (Element i:dList){
			doc.getRootElement().addContent(i);
		}
		
	}
	
	//This method creates and attaches photo's from flickr
	//into the dynamically generated elements
	public void attachPhotos(Element node,Double lati, Double longi, String term){
		//creating photo element
		Element photos = new Element("photos");
		
		//creating new flickr object with api key
		Flickr flick = new Flickr("86e322bff415a41cb2f6f781b1ab053e");
		//creating search parameter object
		SearchParameters search = new SearchParameters();
		//setting the latitude and longitude i am searching for
		search.setLatitude(""+lati);
		search.setLongitude(""+longi);
		//setting search terms
		String[] tags = new String[]{term,"cinema"};
		search.setTags(tags);
		
		try {
			//populating photolist with 5 result images
			PhotoList pics = flick.getPhotosInterface().search(search, 5, 1);
			for (int i=0; i<pics.size();i++){
				Photo pic = (Photo) pics.get(i);
				
				//creating a photo element for each pic
				Element photo = new Element("photo");
				
				//seting it's attribute to url of the pics
				photo.setAttribute("url_s",""+pic.getSmallUrl());
				photo.setAttribute("url_t", ""+pic.getThumbnailUrl());
				
				//adding each photo element to thier parent element
				//photos
				photos.addContent(photo);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlickrException e) {
			// TODO Auto-generatked catch block
			e.printStackTrace();
		}
		//adding photos to the dynamically generated element
		node.addContent(photos);
	}
	

}
