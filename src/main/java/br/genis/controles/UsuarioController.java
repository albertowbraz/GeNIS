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
public class UsuarioController {

	private UsuarioServico dao;
	private Result result;

	/**
	 * @deprecated CDI eyes only
	 */
	public UsuarioController() {
		this(null, null);
	}

	@Inject
	public UsuarioController(Result result, UsuarioServico dao) {
		this.result = result;
		this.dao = dao;
	}

	@Path("/verifica-login")
	public void efetuaLogin(Usuario usuario, HttpSession session) {
		
		Usuario user = dao.verificaLogin(usuario.getLogin());
		boolean tudoOk = false;
		
		if (user != null) {
			
			String senha = "";
			
			if (usuario.getSenha() != null) {
				senha = ServicoMD5.criptografar(usuario.getSenha());
			}

			if (user.getSenha().equals(senha)) {
				session.setAttribute("usuarioLogado", user);
				result.redirectTo(IndexController.class).home();
				tudoOk = true;
			} else {
				result.include("mensagem", "Senha Inválida.");
			}

		} else {
			result.include("mensagem", "Usuário Inválido.");
		}
		
		if (!tudoOk) {
			result.redirectTo(IndexController.class).index();
		}

	}

	@Path("/salva-usuario")
	public void salvar(Usuario usuario) {
		String mensagem = "";
		try {
			dao.salvar(usuario);
			mensagem = "Usuário salvo com sucesso.";
		} catch (ServicoBaseException e) {
			e.printStackTrace();
			mensagem = "Erro ao salvar...";
		}

		result.include("message", mensagem);
		result.redirectTo(IndexController.class).home();

	}

}
