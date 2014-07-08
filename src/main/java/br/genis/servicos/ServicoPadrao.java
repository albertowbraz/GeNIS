/**
 * maciel.dev@gmail.com
 */
package br.genis.servicos;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.Criteria;
import org.hibernate.Session;

import br.genis.modelos.ModeloBase;

/**
 * Classe responsável por garantir as principais funções de CRUD da camada de
 * serviço. Todas as classes de serviço deverão extender dela e implementar o
 * método validar(T entidade) a fim de garantir a consistência e regras de
 * negócio das entidades que vão para o banco de dados. Qualquer regra de
 * negócio específica deverá ser implementada na classe de serviço específica.
 * 
 * @author Maciel Melo
 * @since 05/07/2014
 */

@Stateless
public abstract class ServicoPadrao<T extends ModeloBase<?>> implements
	ServicoBase<T> {
	
	
	@PersistenceContext(unitName = "GeNIS")
    public EntityManager em;
	
    /**
     * Este método recebera uma entidade como parâmetro de deve checar a
     * validade da mesma aplicando todas as regras de negócios inerentes a
     * entidade e seus valores.
     */
    abstract Boolean validar(T entidade) throws ServicoBaseException;

    @Override
    @Transactional(value = TxType.MANDATORY)
    public void salvar(T entidade) throws ServicoBaseException {
	if (validar(entidade)) {
	    try {
		if (entidade.getId() == null) {
		    em.persist(entidade);
		} else {
			em.merge(entidade);
		}
	    } catch (Exception e) {
		throw new ServicoBaseException(
			"Erro ao executar uma operação para salvar a entidade: "
				+ entidade.getClass().getSimpleName() + " ID: "
				+ entidade.getId(), e);
	    }
	}
    }

    @Override
    @Transactional(value = TxType.MANDATORY)
    public void deletar(T entidade) throws ServicoBaseException {
	try {
		em.remove(entidade);
	} catch (Exception e) {
	    throw new ServicoBaseException(
		    "Erro ao executar uma operação para remoção da entidade: "
			    + entidade.getClass().getSimpleName() + " ID: "
			    + entidade.getId(), e);
	}
    }

    @Override
    @Transactional
    public T getById(Object id) throws ServicoBaseException {
	T entidade = em.find(getClasseReal(), id);
	if (entidade == null) {
	    throw new ServicoBaseException(
		    "Não foi encontrado nenhuma entidade no banco de dados. Classe: "
			    + getClasseReal().getSimpleName() + " ID: "
			    + id.toString());
	}

	return entidade;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getClasseReal() throws ServicoBaseException {
	Type tipo = this.getClass().getGenericSuperclass();
	Class<T> classeReal = null;
	try {
	    if (tipo instanceof ParameterizedType) {
		ParameterizedType generico = (ParameterizedType) tipo;

		// Pega a primeira ocorrência porque o serviço só possui 1 tipo
		classeReal = (Class<T>) generico.getActualTypeArguments()[0];
	    }
	} catch (Exception e) {
	    throw new ServicoBaseException(
		    "Erro na tentativa de recuperar a Classe a que o genérico faz referência",
		    e);
	}

	return classeReal;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<T> getAll() throws ServicoBaseException {
	String ql = "from " + getClasseReal().getSimpleName();
	Query query = em.createQuery(ql);
	List<T> result = new ArrayList<T>();
	try {
	    result = query.getResultList();
	} catch (Exception e) {
	    throw new ServicoBaseException(
		    "Erro ao buscar todas as entidades da tabela "
			    + getClasseReal().getSimpleName(), e);
	}
	return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<T> buscarPorCampos(Map<String, Object> campos,
	    Boolean exclusivo, int maxResultados, String orderBy)
	    throws ServicoBaseException {
	String ql = "from " + getClasseReal().getSimpleName() + " t ";
	StringBuilder builder = new StringBuilder(ql);
	String argumento = "";
	String conector = " where ";
	List<T> result = new ArrayList<T>();

	if (campos != null && !campos.isEmpty()) {
	    for (String atributo : campos.keySet()) {
		// TODO estudar melhor essa porra toda
		argumento = atributo.replace(".", "");
		if (campos.get(atributo) instanceof String) {
		    String strCampo = (String) campos.get(atributo);
		    if ("is null".equalsIgnoreCase(strCampo)) {
			builder.append(conector + "t." + atributo + " is null");
		    } else if ("is not null".equalsIgnoreCase(strCampo)) {
			builder.append(conector + "t." + atributo
				+ " is not null");
		    } else if (strCampo.startsWith("like")) {
			builder.append(conector + "lower(t." + atributo
				+ ") like lower(:" + argumento + ")");
		    } else if (strCampo.startsWith("!=")) {
			builder.append(conector + "t." + atributo + "!=:"
				+ argumento);
		    } else {
			builder.append(conector + "t." + atributo + "=:"
				+ argumento);
		    }
		} else {
		    builder.append(conector + "t." + atributo + "=:"
			    + argumento);
		}

		if (exclusivo) {
		    conector = " and ";
		} else {
		    conector = " or ";
		}
	    }

	    if (orderBy != null && !"".equals(orderBy.trim())) {
		builder.append(" order by t." + orderBy);
	    }

	    Query query = em.createQuery(builder.toString());

	    if (maxResultados > 0) {
		query.setMaxResults(maxResultados);
		argumento = "";
		for (String atributo : campos.keySet()) {
		    if (!campos.get(atributo).equals("is null")
			    && !campos.get(atributo).equals("is not null")) {
			argumento = atributo.replace(".", "");
			if (campos.get(atributo) instanceof String) {
			    String campo = (String) campos.get(atributo);
			    if (campo.startsWith("like")) {
				campo = campo.replace("like", "").replace("'",
					"");
				query.setParameter(argumento, campo);
			    } else if (campo.startsWith("!=")) {
				campo = campo.replace("!=", "");
				query.setParameter(argumento, campo);
			    } else {
				query.setParameter(argumento,
					campos.get(atributo));
			    }
			} else {
			    query.setParameter(argumento, campos.get(atributo));
			}
		    }
		}
		result = (List<T>) query.getResultList();
	    }
	}
	return result;
    }
    
	protected Criteria getCriteria(Class<T> modelClass) {
		return montarCriteria(modelClass);
	}

	private Criteria montarCriteria(Class<T> modelClass) {
		Session session = em.unwrap(Session.class);

		String aliasEntityRoot = modelClass.getClass().getSimpleName()
				.substring(0, 1).toLowerCase()
				+ modelClass.getSimpleName().substring(1,
						modelClass.getSimpleName().length());

		Criteria criteria = session.createCriteria(modelClass.getName(),
				aliasEntityRoot);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria;
	}

}
