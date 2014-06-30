/**
 * maciel.dev@gmail.com
 */
package br.genis.servicos;

import java.util.List;
import java.util.Map;

import br.genis.modelos.ModeloBase;

/**
 * Interface base para as classes de serviço.
 * 
 * @author Maciel Melo
 * @since 29/06/2014
 */
public interface ServicoBase<T extends ModeloBase<?>> {

    /**
     * Persiste a entidade no banco de dados.
     * 
     * @param entidade
     * @return {@code true} se não ocorrer nenhum problema durante o processo,
     *         {@code falso} em outros casos.
     */
    public void salvar(T entidade) throws ServicoBaseException;

    /**
     * Remove a entidade do banco de dados.
     * 
     * @param entidade
     *            Objeto entidade que implemente de {@link ModeloBase}
     * @throws ServicoBaseException
     * @return {@code true} se não ocorrer nenhum problema durante o processo,
     *         {@code falso} em outros casos.
     */
    public void deletar(T entidade) throws ServicoBaseException;

    /**
     * Recupera do banco de dados uma entidade através do objeto que represente
     * a chave primária (ID) dela. </br></br><strong>OBS:</strong> lembrar de
     * tratar casos NULOs para evitar NullPonterException
     * 
     * @param entidade
     * @throws ServicoBaseException
     * @return A entidade que estava salva no banco de dados ou um bojeto NULO
     */
    public T getById(Object id) throws ServicoBaseException;

    /**
     * Recupera todas as entidades do banco de dados
     * 
     * @throws ServicoBaseException
     * @return Uma lista com todas as entidades encontradas no banco ou uma
     *         lista vazia.
     */
    public List<T> getAll() throws ServicoBaseException;

    /**
     * 
     * @param campos
     *            Atributo e Valor a Ser Comparado
     * @param exclusivo
     *            Se for marcado com TRUE usará o operador de conjunção 'AND',
     *            caso FALSE usará OR.
     * @param maxResultados
     *            define o máximo de entidades que devem ser recuperados
     * @param orderBy
     *            odernação através de um atributo
     * @throws ServicoBaseException
     * @return
     */
    public List<T> buscarPorCampos(Map<String, Object> campos,
	    Boolean exclusivo, int maxResultados, String orderBy) throws ServicoBaseException;
}
