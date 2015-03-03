package Mashup;

import java.sql.*;
import java.util.ArrayList;

import org.jdom2.Element;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Extras;
import com.aetrion.flickr.photos.GeoData;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.SearchParameters;
 
public class DAO {
	
	private ArrayList<Element> nList = new ArrayList<Element>();
	public DAO(){
		
	}
	
	public ArrayList<Element> getNodeList(){
		return nList;
	}
	
	public void photoProcess(){
		Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  String url = "jdbc:mysql://localhost:3306/";
		  String dbName = "cinemas";
		  String driver = "com.mysql.jdbc.Driver";
		  String userName = "root"; 
		  String password = "";
		  try {
		  Class.forName(driver).newInstance();
		  conn = DriverManager.getConnection(url+dbName,userName,password);
		  System.out.println("Connected to the database IMAGE");
		  stmt = conn.createStatement();
		  
		  rs = stmt.executeQuery("SELECT * FROM pictures");
		  
		  //INSERTING PHOTOS INTO DATABASE-----------------------------------
		  if(rs.next()==false){
		  for(Element node : nList){
			
			String iD = node.getAttributeValue("id");
			
			stmt.executeUpdate("insert into pictures(ID)" +
	  		"values (\""+iD+"\")");
			
			Flickr flick = new Flickr("86e322bff415a41cb2f6f781b1ab053e");
			SearchParameters search = new SearchParameters();
			search.setLatitude(node.getChildText("latitude"));
			search.setLongitude(node.getChildText("longitude"));
			String[] tags = new String[]{"shopping","cinema"};
			search.setTags(tags);
			
				
			PhotoList pics = flick.getPhotosInterface().search(search, 5, 1);
			for (int i=0; i<pics.size();i++){
				Photo pic = (Photo) pics.get(i);
				String uRl = pic.getThumbnailUrl()+"*"+pic.getSmallUrl();
				
				if(i==0){
					String sqll = "UPDATE pictures SET photo1=\""+uRl+"\" WHERE ID="+iD;
					stmt.executeUpdate(sqll);
				}
				else if(i==1){
					String sqll = "UPDATE pictures SET photo2=\""+uRl+"\" WHERE ID="+iD;
					stmt.executeUpdate(sqll);
				}
				else if(i==2){
					String sqll = "UPDATE pictures SET photo3=\""+uRl+"\" WHERE ID="+iD;
					stmt.executeUpdate(sqll);
				}
				else if(i==3){
					String sqll = "UPDATE pictures SET photo4=\""+uRl+"\" WHERE ID="+iD;
					stmt.executeUpdate(sqll);
				}
				else if(i==4){
					String sqll = "UPDATE pictures SET photo5=\""+uRl+"\" WHERE ID="+iD;
					stmt.executeUpdate(sqll);
				}
					
			}
			
			
		  }
		  //---------------------------------------------------
		  }
		  
		  
		  //READING PHOTOS FROM DATABASE----------------------------------------------
		  rs = stmt.executeQuery("SELECT * FROM pictures");
		  while(rs.next()){
			  String databaseID = rs.getString("ID");
			  for(Element node2 : nList){
				  String nodeID = node2.getAttributeValue("id");
				  if(databaseID.equals(nodeID)){
					  Element photos = new Element("photos");
					  
					  String photo1 = rs.getString("photo1");
					  String[] p = photo1.split("\\*");
					  Element photoEl = new Element("photo");
					  photoEl.setAttribute("url_t",p[0]);
					  photoEl.setAttribute("url_s",p[1]);
					  photos.addContent(photoEl);
					  
					  
					  String photo2 = rs.getString("photo2");
					  String[] q = photo2.split("\\*");
					  photoEl = new Element("photo");
					  photoEl.setAttribute("url_t",q[0]);
					  photoEl.setAttribute("url_s",q[1]);
					  photos.addContent(photoEl);
					  
					  String photo3 = rs.getString("photo3");
					  String[] r = photo3.split("\\*");
					  photoEl = new Element("photo");
					  photoEl.setAttribute("url_t",r[0]);
					  photoEl.setAttribute("url_s",r[1]);
					  photos.addContent(photoEl);
					  
					  String photo4 = rs.getString("photo4");
					  String[] s = photo4.split("\\*");
					  photoEl = new Element("photo");
					  photoEl.setAttribute("url_t",s[0]);
					  photoEl.setAttribute("url_s",s[1]);
					  photos.addContent(photoEl);
					  
					  String photo5 = rs.getString("photo5");
					  String[] t = photo5.split("\\*");
					  photoEl = new Element("photo");
					  photoEl.setAttribute("url_t",t[0]);
					  photoEl.setAttribute("url_s",t[1]);
					  photos.addContent(photoEl);
					  
					  node2.addContent(photos);
				  }
			  }
		  }
		  //------------------------------------------------------
		  
		  //DELETING FROM DATABASE-----------------------------------
		  String delete = "DELETE FROM pictures WHERE time BETWEEN \"2012-11-30\" AND CURDATE()";
		  stmt.executeUpdate(delete);
		
		  conn.close();
		  System.out.println("Disconnected from database IMAGE");
		  } catch (Exception e) {
		  e.printStackTrace();
		  }
	}
	
	public void createNodeList(){
		  Connection conn = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  String url = "jdbc:mysql://localhost:3306/";
		  String dbName = "cinemas";
		  String driver = "com.mysql.jdbc.Driver";
		  String userName = "root"; 
		  String password = "";
		  try {
		  Class.forName(driver).newInstance();
		  conn = DriverManager.getConnection(url+dbName,userName,password);
		  System.out.println("Connected to the database");
		  
		  
		//READING FROM DATABASE-----------------------------
		  
		  stmt = conn.createStatement();
		  rs = stmt.executeQuery("SELECT * FROM cinema");
			while (rs.next()) {
				Element cinema = new Element("cinema");
				
				String iD = rs.getString("house ID");
				cinema.setAttribute("id", iD);
				
				String name = rs.getString("name");
				Element nameEl = new Element("name");
				nameEl.setText(name);
				cinema.addContent(nameEl);
				
				String address = rs.getString("address");
				Element addressEl = new Element("address");
				addressEl.setText(address);
				cinema.addContent(addressEl);
				
				String postalCode = rs.getString("postal code");
				Element postalCodeEl = new Element("postalcode");
				postalCodeEl.setText(postalCode);
				cinema.addContent(postalCodeEl);
				
				String phone = rs.getString("phone");
				Element phoneEl = new Element("phone");
				phoneEl.setText(phone);
				cinema.addContent(phoneEl);
				
				//mainphoto processing-------------
				String photo = rs.getString("mainphoto");
				String[] p = photo.split("\\*");
				String imgId = p[0];
				String imgUrl = p[1];
				Element mainPhoto = new Element("mainphoto");
				mainPhoto.setAttribute("id", imgId);
				mainPhoto.setAttribute("url", imgUrl);
				cinema.addContent(mainPhoto);
				//---------------------------------
				
				//amenities processing--------------
				String amenityString = rs.getString("amenities");
				String[] q = amenityString.split("\\*");
				Element amenities = new Element("amenities");
				for(int i=0;i<q.length;i++){
					Element amenity = new Element("amenity");
					amenity.setAttribute("amen", q[i]);
					amenities.addContent(amenity);
				}
				cinema.addContent(amenities);
				//----------------------------------
				
				//admissions processing--------------
				String admString = rs.getString("admissions");
				String[] r = admString.split("\\*");
				Element admissions = new Element("admissions");
				for(int j=0;j<r.length;j++){
					String[] sub = r[j].split("#");
					Element admission = new Element("admission");
					admission.setAttribute("price", sub[0]);
					admission.setAttribute("target", sub[1]);
					admissions.addContent(admission);
				}
				cinema.addContent(admissions);
				//-----------------------------------
				
				String lati = rs.getString("latitude");
				Element latitude = new Element("latitude");
				latitude.setText(lati);
				cinema.addContent(latitude);
				
				String longi = rs.getString("longitude");
				Element longitude = new Element("longitude");
				longitude.setText(longi);
				cinema.addContent(longitude);
				
				nList.add(cinema);
				
			}
		
		 
		  conn.close();
		  System.out.println("Disconnected from database");
		  } catch (Exception e) {
		  e.printStackTrace();
		  }
		
	}
	
	

}

