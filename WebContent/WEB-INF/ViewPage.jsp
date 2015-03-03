<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link href="http://localhost:10692/IAT_352_Assignment_2/cssjs/mapstyle.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyCsqQHbdLh1ugput323vn9F2Qt-dGe7Tsk&sensor=false"></script>
	<script type="text/javascript" src="http://localhost:10692/IAT_352_Assignment_2/cssjs/downloadxml.js"></script>
	
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Main view Page</title>
	<script type="text/javascript" >
		//initializing variables
		//This stores the google map
		var map;
		
		//This stores the generated xml document
		var xml2;
		
		//This stores all the markers on the map
		var myMarkers = new Array();
		
		//this is the info window object
		var infowindow = new google.maps.InfoWindow({content: ""});
		
		//this is used to check if the body of the document
		//has been created once
		var loaded;
		
		//this stores the bounds of the google map
		var bounds;
		
		//this stores the xslt file
		var xsl=loadXMLDoc("http://localhost:10692/IAT_352_Assignment_2/xmlFiles/cinemas.xsl");
		function initialize(){
			loaded = false;
			
			//creating a new map and appending it to the div "myMap"
			var mapOptions = {
				center: new google.maps.LatLng(49.0,-122.0),
				zoom: 10,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			};
			map = new google.maps.Map(document.getElementById("myMap"),mapOptions);
			
			loaded = true;
			
			//adding drag end event listener to the map
			google.maps.event.addListener(map, 'dragend', function() {
				
				bounds = map.getBounds();
				var center = bounds.getCenter();
				var ne = bounds.getNorthEast();

				// r = radius of the earth in statute miles
				var r = 3963.0;  

				// Convert lat or lng from decimal degrees into radians (divide by 57.2958)
				var lat1 = center.lat() / 57.2958; 
				var lon1 = center.lng() / 57.2958;
				var lat2 = ne.lat() / 57.2958;
				var lon2 = ne.lng() / 57.2958;
				

				// distance = circle radius from center to Northeast corner of bounds
				var dis = r * Math.acos(Math.sin(lat1) * Math.sin(lat2) + 
				  Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1));
			
				//displays the body of the page
				displayResult(center.lat(),center.lng(),dis/100,"shopping");
			});
			//adding zoom changed event listener to the map
			google.maps.event.addListener(map, 'zoom_changed', function() {
				
				bounds = map.getBounds();
				
				var center = bounds.getCenter();
				var ne = bounds.getNorthEast();

				// r = radius of the earth in statute miles
				var r = 3963.0;  

				// Convert lat or lng from decimal degrees into radians (divide by 57.2958)
				var lat1 = center.lat() / 57.2958; 
				var lon1 = center.lng() / 57.2958;
				var lat2 = ne.lat() / 57.2958;
				var lon2 = ne.lng() / 57.2958;
				
				// distance = circle radius from center to Northeast corner of bounds
				var dis = r * Math.acos(Math.sin(lat1) * Math.sin(lat2) + 
				  Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1));
				
				//displays the body of the page
				displayResult(center.lat(),center.lng(),dis/100,"shopping");
			});
			
		}
		function loadXMLDoc(dname) {
			if (window.XMLHttpRequest) {
			  	xhttp=new XMLHttpRequest();
			}
			else {
			  	xhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			
			xhttp.open("GET",dname,false);
			xhttp.send("");
			return xhttp.responseXML;
		}
		//This function displays the body of the page and calling the servlet each time
		//it is called
		function displayResult(lat,longi,radius,term) {
			
			//getting xml document from the servlet based on the parameters sent
			xml2=loadXMLDoc("http://localhost:10692/IAT_352_Assignment_2/test1?latitude="+lat+"&longitude="+longi+"&radius="+radius+"&term="+term);
			
			// code for IE
			if (window.ActiveXObject) {
			  	var ex=xml2.transformNode(xsl);
			  	document.getElementById("xsl2").innerHTML=ex;
			  }
			// code for Mozilla, Firefox, Opera, etc.
			else if (document.implementation && document.implementation.createDocument) {
				
				//Loading the xslt processor
			  	var xsltProcessor=new XSLTProcessor();
			  	xsltProcessor.importStylesheet(xsl);
			  	
			  	//if this is the first time the page is loaded
			  	if(loaded==false){
			  		//transform the generated xml document and place it in the div "xsl2"
			  		var resultDocument = xsltProcessor.transformToFragment(xml2,document);
				  	document.getElementById("xsl2").appendChild(resultDocument);
			  	}
			  	//if not
			  	else{
			  		//clear all the markers in the markers array
			  		for(var k=0;k<myMarkers.length;k++){
			  			myMarkers[k].setMap(null);
			  		}
			  		//transform the result xml
			  		var resultDocument = xsltProcessor.transformToFragment(xml2,document);
			  		
			  		//remove the existing result by deleting the div "xsl2 from the document
			  		var rem = document.getElementById("xsl2");
			  		document.getElementById("xslbody").removeChild(rem);
			  		
			  		//create a new div with the same name and append
			  		//the new content to it
			  		var newdiv=document.createElement("div");
			  		var attr = document.createAttribute('id');
			  		attr.nodeValue = 'xsl2';
			  		newdiv.setAttributeNode(attr);
			  		document.getElementById("xslbody").appendChild(newdiv);
			  		document.getElementById("xsl2").appendChild(resultDocument);
			  		
			  		
			  		
			  	}
			  	
			 }
			//getting the tags from the xml file
			var lati = xml2.getElementsByTagName("latitude");
			var longi = xml2.getElementsByTagName("longitude");
			
			var names = xml2.getElementsByTagName("name");
			var address = xml2.getElementsByTagName("address");
			var mainphotos = xml2.getElementsByTagName("mainphoto");
			var photos = xml2.getElementsByTagName("photos");
			
			for(var i=0;i<lati.length;i++){
				//getting latitude and longitude coordinates for each element in the document
				var cord=new google.maps.LatLng(parseFloat(lati[i].childNodes[0].nodeValue),parseFloat(longi[i].childNodes[0].nodeValue));
				
				//generating info window content for each element in the document
				var content='<p><b>'+names[i].childNodes[0].nodeValue+'</b></p>'
				+'<p>'+address[i].childNodes[0].nodeValue+'</p>'
				+'<p><img src="'+mainphotos[i].getAttribute("url")+'" width="140"/></p>'
				+'<p><a href="#'+mainphotos[i].getAttribute("id")+'">more information</a></p>'+'<p>surrounding area:</p>';
				
				//and also getting all the photos
				for(var j=0;j<photos[i].childNodes.length;j++){
					content = content+'<a href="'+photos[i].childNodes[j].getAttribute("url_s")+'" target="_blank"><img src="'+photos[i].childNodes[j].getAttribute("url_t")+'"/></a>'
					+'<span style="padding-left:10px"></span>';
				}
				
				//creatinga new marker for each element in the document
				var marker = new google.maps.Marker({position:cord,map:map,html:content});
				
				//adding click event listener for them to show their infowindows
				//when clicked
				google.maps.event.addListener(marker, 'click', function() {
					infowindow.setContent(this.html);
					infowindow.open(map,this);
				});
				
				//adding each marker to the array
				myMarkers.push(marker);
			}
			
		}
		
	</script>
</head>
<body onload="initialize()">
	<table border="1">
		<tr>
			<td>
				<div id="myMap"></div>
			</td>
			<td><b>MAP VIEW</b></td>
		</tr>
		<tr>
			<td>
				<div id= "xslbody">
					<div id="xsl2"></div>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>