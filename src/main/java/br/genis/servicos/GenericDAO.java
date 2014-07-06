package br.genis.servicos;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

@SuppressWarnings({ "unchecked" })
@Stateless
public class GenericDAO<T> implements Serializable {

	private static final long serialVersionUID = 8913854433547892847L;

	@PersistenceContext
	public EntityManager em;
	
	public void setEm(EntityManager em) {
		this.em = em;
	}

	/**
	 * 
	 * @param bean
	 *            se null retorna findAll
	 * @return
	 */

	public T findById(Class<T> modelClass, Object id,
			Boolean initializeDependecies) {
		System.out.println("Id do Objeto " + id);
		System.out.println("Class " + modelClass.getName());
		T t = em.find(modelClass, id);
		return (T) (t != null && initializeDependecies ? initializeDependecies(t)
				: t);
	}

	public Object initializeDependecies(Object t) {

		Method[] methods = t.getClass().getMethods();
		for (Method method : methods) {
			Class<?> returnTypeClass = method.getReturnType();
			if (returnTypeClass.isAnnotationPresent(Entity.class)
					|| Collection.class.isAssignableFrom(returnTypeClass)) {
				try {
					Object object = method.invoke(t);
					if (object != null) {
						object.toString();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return t;
	}

	public List<T> initializeDependecies(List<T> lista) {
		for (T o : lista) {
			initializeDependecies(o);
		}
		return lista;
	}

	/**
	 * 
	 * @param bean
	 *            se null retorna findAll
	 * @return
	 */
	public List<T> findAll(Class<T> modelClass, Boolean initializeDependecies) {
		if (modelClass == null) {
			throw new IllegalArgumentException("class não pode ser null!");
		}

		Criteria criteria = getCriteria(modelClass);
		criteria.addOrder(Order.asc("id"));

		List<T> lista = criteria.list();

		if (initializeDependecies) {
			for (T t : lista) {
				initializeDependecies(t);
			}
		}

		return lista;
	}

	/**
	 * 
	 * @param bean
	 *            se null retorna findAll
	 * @return
	 */
	public List<T> example(T bean, Boolean initializeDependecies) {
		Criteria criteria = getCriteria(bean);

		Example example = Example.create(bean);
		example.excludeZeroes();
		example.enableLike(MatchMode.ANYWHERE).ignoreCase();
		criteria.add(example);
		List<T> list = criteria.list();

		if (initializeDependecies) {
			for (T t : list) {
				initializeDependecies(t);
			}
		}

		return list;
	}

	protected Criteria getCriteria(Class<T> modelClass) {
		return montarCriteria(modelClass);
	}

	protected Criteria getCriteria(T bean) {
		return montarCriteria((Class<T>) bean.getClass());
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

	public List<T> findAll(T modelClass, Integer startPage, Integer maxPage,
			String orderField, Boolean initializeDependecies) {

		Criteria criteria = getCriteria((Class<T>) modelClass.getClass());
		criteria.setFirstResult(startPage);
		criteria.setMaxResults(maxPage);

		if (orderField != null && !orderField.trim().equals(""))
			criteria.addOrder(Order.asc(orderField));

		List<T> lista = criteria.list();
		if (initializeDependecies) {
			for (T t : lista) {
				initializeDependecies(t);
			}
		}

		return lista;
	}

	public Long count(T modelClass) {
		Long size = 0L;
		Criteria criteria = getCriteria((Class<T>) modelClass.getClass());
		criteria.setProjection(Projections.rowCount());
		size = (Long) criteria.uniqueResult();
		return size;
	}

	// ********************************CRUD************************************

	public void delete(T objeto) {
		em.remove(em.merge(objeto));
	}

	public T insert(T objeto) {
		em.persist(objeto);
		return objeto;
	}

	public T insertOrUpdate(T objeto) {
		em.merge(objeto);// Se existir no banco, ele atualiza, se não existir,
							// ele insere;
		return objeto;

	}

	public T update(T t, Boolean updateDependecies) {
		if (updateDependecies) {
			t = lazyInitialize(t);
		}
		em.merge(t);
		return t;
	}

	public T update(T t) {
		return update(t, true);
	}

	/**
	 * Busca entities relacionados ao 'source' de forma a atribuir os valores de
	 * seus IDs que estão "proxiados" ao 'source'. Esse método considera
	 * respeito ao padrão Java Bean e a seguinte convenção em Entity: métodos:
	 * 'getId' e 'setId'.
	 * 
	 * @param source
	 * @return source modificado
	 */
	protected T lazyInitialize(T source) {
		Method[] methods = source.getClass().getMethods();
		for (Method method : methods) {

			Class<?> returnTypeClass = method.getReturnType();
			if (returnTypeClass.isAnnotationPresent(Entity.class)) {
				try {
					// obter proxy
					Object objValue = method.invoke(source);
					if (objValue != null) {
						// obter ID do objeto proxy
						Object idValue = objValue.getClass().getMethod("getId")
								.invoke(objValue);

						// criar nova instância do mesmo tipo do retorno do
						// método get
						Object newEntity = returnTypeClass.newInstance();
						// setar id para nova instância vazia
						newEntity.getClass()
								.getMethod("setId", idValue.getClass())
								.invoke(newEntity, idValue);

						// substitui proxy pela instância nova (povoada apenas
						// com ID)
						source.getClass()
								.getMethod(
										method.getName().replace("get", "set"),
										returnTypeClass)
								.invoke(source, newEntity);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return source;
	}

}
