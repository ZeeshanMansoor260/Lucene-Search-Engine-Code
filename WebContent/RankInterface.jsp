<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Page rank</title>
<meta name=viewport content=width=device-width, initial-scale=1>
<link rel=stylesheet href=https:www.w3schools.com/w3css/4/w3.css>
<link rel=stylesheet href=https:/cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css>
	    	
<style>
B{
	color: red;
}
	       
</style>
</head>

<body>
       <div class = "w3-container">+
	       			
	    		   <form action="searcher1" method="post">
	    		   <div class="w3-container  w3-display-topleft w3-pale-green">
	    		   
	    		   <h3>Lucene Search <input type="text"  name="search" size = "60">
	    		   <button class="w3-button w3-xlarge w3-circle w3-teal" type ="submit"><i class="fa fa-search"></i></button></h3> 
	    		   </div>
	    		  <div class="w3-cell-row w3-padding-48">
	    		  <div class = "w3-cell w3-cell-top w3-padding-large "> Search By: <br>
	    		  <!--<input type="radio" name="option" value="CiteSeer" checked> CiteSeer
  					<input type="radio" name="option" value="Sigmod"> Sigmod
  					<input type="radio" name="option" value="Microsoft"> Microsoft -->
	    		   <input type="radio" name="option" value="all" checked> All <br> 
	    		   <input type="radio" name="option" value="authors"> Authors <br>
	    		   <input type="radio" name="option" value="venue"> Venue <br> 
	    		   <input type="radio" name="option" value="year"> Year 
	    		    

 
							
	    		   </div>
	    		   
	    		   </form>
	    		   
	    		   
	       <div class = "w3-cell w3-cell-middle">
	       <p>
	      Time Taken:  ${result[0]}   seconds<br><h3>Similar Queries:</h3><h4 style="color:blue"> ${result[1]}</h4><p>  ${result[2]} </p>
	       		</div></div>
	       		</div>
	       		<a href = "index.jsp">Back</a>
</body>
</html>