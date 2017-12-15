<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Information Retrieval Systems</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

</head>
<body>

<div class="w3-container w3-display-middle w3-pale-green" style="text-align: center;">
	<h1>Lucene Search</h1>

<form action="searcher1" method="post">
  Enter Search:
  <input type="text" name="search" size = "60">
  <button class="w3-button w3-xlarge w3-circle w3-teal" type ="submit"><i class="fa fa-search"></i></button>
  <br>
  <input type="radio" name="option" value="CiteSeer" checked> CiteSeer
  <input type="radio" name="option" value="Sigmod"> Sigmod
  <input type="radio" name="option" value="Microsoft"> Microsoft 
  
  
  
</form>
	Features
	<a href="doc2vec.jsp">Doc2Vec</a>
</div>
</body>
</html>