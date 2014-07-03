package br.genis.servicos;

import br.genis.modelos.Usuario;

public class UsuarioDao extends ServicoPadrao<Usuario>{

	@Override
	Boolean validar(Usuario entidade) throws ServicoBaseException {
		return true;
	}

}
