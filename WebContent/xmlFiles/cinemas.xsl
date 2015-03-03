
<xsl:stylesheet  version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cinemaLog="http://www.cinema.com/cinemas" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

	<xsl:template match="/">
		<html>
        	<head>
            </head>
			<body>
				<table border="1">
					<xsl:for-each select="cinemas/cinema">
						<tr>
							<td>
                            	<img width="500" src="{mainphoto/@url}" id="{mainphoto/@id}"/>
                                
                                
                            </td>
							<td>
								<p>NAME: <b><xsl:value-of select="name"/></b></p>
								<p>ADDRESS: <xsl:value-of select="address"/></p>
								<p>POSTAL CODE: <xsl:value-of select="postalcode"/></p>
								<p>PHONE NO: <xsl:value-of select="phone"/></p>
								
								<p>AMENITIES:</p>
								<xsl:for-each select="amenities/amenity">
									<ul>
										<li><xsl:value-of select="@amen"/></li>
									</ul>
								</xsl:for-each>
								
								<p>ADMISSIONS:</p>
								<xsl:for-each select="admissions/admission">
									<ul>
										<li><xsl:value-of select="@target"/> |PRICE: <xsl:value-of select="@price"/></li>
									</ul>
								</xsl:for-each>
								
								<p>Pictures of sorroundings:</p>
								<p>
									<xsl:for-each select="photos/photo">
										<a href="{@url_s}" target="_blank"><img src="{@url_t}"/></a><span style="padding-left:20px"></span>
									</xsl:for-each>
								</p>
							</td>
						</tr>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>