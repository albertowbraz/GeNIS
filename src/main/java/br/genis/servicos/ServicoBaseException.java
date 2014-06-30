/**
 * maciel.dev@gmail.com
 */
package br.genis.servicos;

/**
 * Representação de qualquer fluxo de exceção não previsto dentro do pacote de
 * serviço dos dados.
 * 
 * @author Maciel Melo
 * @since 29/06/2014
 */
public class ServicoBaseException extends Exception {

    public ServicoBaseException(String message, Throwable cause) {
	super(message, cause);
    }

    public ServicoBaseException(String message) {
	super(message);
    }

    private static final long serialVersionUID = 3686750819739519546L;

}
