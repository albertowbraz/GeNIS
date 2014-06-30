/**
 * maciel.dev@gmail.com
 */
package br.genis.modelos;

import java.io.Serializable;

/**
 * Interface que toda classe entidade deverá implementar. Esse modelo possui
 * apenas dois métodos para garantir que o id será recuperado nas classes de
 * serviço. Está classe já extende Serializable para garantir o corretor
 * funcionamento do JPA</br></br>
 * 
 * T será o tipo de dado referente ao Id. Pode ser tipos já existentes no java
 * ou então uma classe gerada para IDs compostos com a anotação 'Embeddable'
 * </br></br> Exemplo: </br>
 * {@code public class EntidadeExemplo extends ModelBase<Long>}
 * 
 * @author Maciel Melo
 * @since 06/2014
 */
public interface ModeloBase<T> extends Serializable {

    public T getId();

    public void setId();
}
