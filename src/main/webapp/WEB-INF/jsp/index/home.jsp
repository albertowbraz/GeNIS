<%@ include file="../../../header.jsp"%>

	<form class="form" action="<c:url value='/salva-usuario' /> ">
		<input type="text" name="usuario.login" placeholder="Login" >
		<input type="text" name="usuario.senha" placeholder="Login" >
		<input type="submit" value="Salvar" >

	</form>

<%@ include file="../../../footer.jsp"%>
