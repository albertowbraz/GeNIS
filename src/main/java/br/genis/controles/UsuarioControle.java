package br.genis.controles;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.genis.modelos.Usuario;
import br.genis.servicos.ServicoBaseException;
import br.genis.servicos.ServicoMD5;
import br.genis.servicos.UsuarioServico;

@Controller
public class UsuarioControle {
	
	private UsuarioServico dao;
	private Result result;

	/**
	 * @deprecated CDI eyes only
	 */
	public UsuarioControle() {
		this(null, null);
	}
	
	@Inject
	public UsuarioControle(Result result, UsuarioServico dao) {
		this.result = result;
		this.dao = dao;
	}
	
	@Path("/verifica-login")
	public void efetuaLogin(Usuario usuario, HttpSession session){
		Usuario user = dao.verificaLogin(usuario.getLogin(), usuario.getSenha());

		if (user != null) {
			String senha = ServicoMD5.criptografar(usuario.getSenha());
			
			if (user.getSenha().equals(senha)) {
				session.setAttribute("usuarioLogado", user);
				result.redirectTo(IndexControle.class).home();
			}
			
			result.include("mensagem", "Senha Inválida.");
			
		} else {
			result.include("mensagem", "Usuário Inválido.");
		}

		result.redirectTo(IndexControle.class).index();

	}
	
	@Path("/salvar")
	public void salvar(Usuario usuario){
		
		try {
			dao.salvar(usuario);
			result.include("message", "Usuário salvo com sucesso.");
		} catch (ServicoBaseException e) {
			e.printStackTrace();
		}
		
		result.redirectTo(IndexControle.class).home();
		
	}
	
}
