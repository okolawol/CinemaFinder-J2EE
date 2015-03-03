package Mashup;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.ProcessingInstruction;
import org.jdom2.output.XMLOutputter;

@WebServlet("/test1")
public class LogicParser extends HttpServlet {
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		
			response.setContentType("text/xml");
		
		//extracting parameter values from the request
		Double lati = Double.parseDouble(request.getParameter("latitude"));
		Double longi = Double.parseDouble(request.getParameter("longitude"));
		Double radius = Double.parseDouble(request.getParameter("radius"));
		String term = request.getParameter("term");
		
		//Creating a new document object
		Document doc = new Document();
		
		try {
			//creating xml Parser object
			XmlParser parser = new XmlParser();
			
			//Populating the dynamic xml with results
			parser.populateDlist(lati,longi, radius,term);
			
			//appending results to the document doc
			parser.appendXml(doc);
			
			//instantiating xml outputter
			XMLOutputter fmt = new XMLOutputter();
			
			//sending the newly generated xml as output
			fmt.output(doc, response.getOutputStream());
		} catch (JDOMException e) {
			// TODO Auto-generated;;; catch block
			e.printStackTrace();
		}
		
		
	}

}
