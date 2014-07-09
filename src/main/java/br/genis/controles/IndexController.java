package br.genis.controles;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;

@Controller
public class IndexController {

	@Path("/")
	public void index() {
	}

	@Path("/home")
	public void home() {
	}
	
}