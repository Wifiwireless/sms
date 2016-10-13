package com.wifiwireless.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.wifiwireless.interfaces.MessagesInterface;
import com.wifiwireless.model.Messages;
import com.wifiwireless.model.NumberDetails;
@Stateless
public class MessagesDao extends PL4BaseDAO implements MessagesInterface,Serializable{
	
	EntityManager em = null;

	/**
	 * Default constructor.
	 */
	
	public void addMesages(Messages messages) {
		em = getEm();

		try {
			em.getTransaction().begin();
			
				em.persist(messages);
			
			em.getTransaction().commit();
		} catch (Exception exception) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			

		} finally {

			em.close();
		}

	}
	public void mergeNumber(Messages messages) {
		em = getEm();

		try {
			
			em.getTransaction().begin();
			em.merge(messages);
			em.getTransaction().commit();
		
		} catch (Exception exception) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			//LOG.error(exception.getMessage());

		} finally {

			em.close();
		}

	}
	
	

	

	

}
