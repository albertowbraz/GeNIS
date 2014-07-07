package br.genis.controles;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.genis.modelos.Usuario;
import br.genis.servicos.UsuarioServico;

@Controller
public class IndexController {

	private UsuarioServico dao;
	private Result result;

	/**
	 * @deprecated CDI eyes only
	 */
	public IndexController() {
		this(null, null);
	}
	
	@Inject
	public IndexController(Result result, UsuarioServico dao) {
		this.result = result;
		this.dao = dao;
	}

	@Path("/")
	public void index() {
	}
	
	@Path("/verificaLogin")
	public void efetuaLogin(Usuario usuario, HttpSession session){
		Usuario user = dao.verificaLogin(usuario.getLogin(), usuario.getSenha());
		
		if (user != null) {
			session.setAttribute("usuarioLogado", usuario);
			result.redirectTo(this).home();
		} else {
			result.include("mensagem", "Usuário ou senha Inválido.");
			result.redirectTo(this).index();
		}
		
	}

	@Path("/home")
	public void home() {
		
	}
	
}