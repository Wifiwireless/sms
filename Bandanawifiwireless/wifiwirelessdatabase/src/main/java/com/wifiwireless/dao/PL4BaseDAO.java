package com.wifiwireless.dao;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


public class PL4BaseDAO {



	private static EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("wifiwirelessdatabase");

	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	public static EntityManager getEm() {

//		LOG.debug(emf);
		
		EntityManager em = getEmf().createEntityManager();
//		LOG.debug("Initialising EntityManager ... ");
		return em;
	}

}