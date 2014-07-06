package br.genis.servicos;

import javax.ejb.Stateless;

import br.genis.modelos.Usuario;

@Stateless
public class UsuarioDao extends GenericDAO<Usuario>{
	
	private static final long serialVersionUID = 657115834272358690L;

	public Usuario verificaLogin(String login, String senha) {
		Usuario usuario = null;

		try {
			
			usuario = em
					.createQuery("select u from Usuario u where u.login = :login and u.senha = :senha", Usuario.class)
						.setParameter("login", login)
						.setParameter("senha", senha)
						.getSingleResult();
			
		} catch (Exception e) {
			// TODO 
			System.out.println(" ################### FALHA AO CARREGAR O USUARIO \n ERROR: " + e);
		}
		return usuario;
	}

}
