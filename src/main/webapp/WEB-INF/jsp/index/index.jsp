<%@ include file="../../../header.jsp"%>

	<form class="form-horizontal" action="<c:url value='/verificaLogin' />" >
		<div class="control-group">
			<label class="control-label" for="inputLogin">Login</label>
			<div class="controls">
				<input type="text" id="inputLogin" name="usuario.login" placeholder="Login">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="inputSenha">Senha</label>
			<div class="controls">
				<input type="password" id="inputSenha" name="usuario.senha" placeholder="Senha">
			</div>
		</div>

		<div class="control-group">
			<div class="controls">
				<button type="submit" class="btn">Entrar</button>
			</div>
		</div>

	</form>

<%@ include file="../../../footer.jsp"%>
