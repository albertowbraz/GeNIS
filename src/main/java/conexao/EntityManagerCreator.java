package conexao;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

public class EntityManagerCreator {
	
	@PersistenceContext
	private EntityManagerFactory factory;

	public EntityManager getEntityManager() {
		return factory.createEntityManager();
	}

	@PreDestroy
	public void destroy(EntityManager em) {
		if (em.isOpen()) {
			em.close();
		}
	}
}