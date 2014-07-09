<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>GeNIS</title>
<link href="<c:url value="/bootstrap/css/bootstrap.css" />"
	rel="stylesheet" type="text/css" />
<link href="<c:url value="/css/genesis.css"/>" rel="stylesheet"
	type="text/css" />

<script type="text/javascript" src="<c:url value='/js/jquery.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/bootstrap/js/bootstrap.js'/>"></script>

</head>
<body>

	<div class="navbar navbar-fixed-top" id="nav-bar">
		<div class="navbar-inner">
				<a class="brand" id="titulo-nav" href="#">GeNIS</a>
				
				<ul class="nav">
					<li class="active"><a href="#">Home</a></li>
					<li><a href="#">Link</a></li>
					<li><a href="#">Link</a></li>
				</ul>
				
		</div>
	</div>
	
<!-- 	gambiarra :) -->
	<br/> <br/>
<!-- 	fim da gambiarra ;) -->

	<div id="contentWrap">

		<div class="well">
			<c:if test="${not empty mensagem}">
				<div class="alert alert-danger alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only"></span>
					</button>
					<strong>Atenção!</strong> ${mensagem}
				</div>
			</c:if>
		</div>