package br.genis.servicos;

import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.genis.modelos.Usuario;

@Stateless
public class UsuarioServico extends ServicoPadrao<Usuario>{
	
	Logger log = LoggerFactory.getLogger(UsuarioServico.class);
	
	@Override
	Boolean validar(Usuario entidade) throws ServicoBaseException {
		return true;
	}
	
	public Usuario verificaLogin(String login, String senha) {
		Usuario usuario = null;

		try {
			
			usuario = em
					.createQuery("select u from Usuario u where u.login = :login", Usuario.class)
						.setParameter("login", login)
						.getSingleResult();
			
			log.info("Usuario carregado: " + usuario.toString());
			
		} catch (Exception e) {
			log.error("Falha ao carregar o usuario - " + e.toString());
		}
		return usuario;
	}
	
	@Override
	public void salvar(Usuario usuario) throws ServicoBaseException {
		if (validar(usuario)) {
			
			String senha = ServicoMD5.criptografar(usuario.getSenha());
			usuario.setSenha(senha);
			
			try {
				if (usuario.getId() == null) {
				    em.persist(usuario);
				} else {
					em.merge(usuario);
				}
			} catch (Exception e) {
				// TODO: 
			}
			
		}
	}

}
