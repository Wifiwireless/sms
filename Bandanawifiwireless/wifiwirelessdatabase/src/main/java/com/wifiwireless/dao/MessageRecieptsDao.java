package com.wifiwireless.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import com.wifiwireless.interfaces.MessageRecieptsInterface;
import com.wifiwireless.model.MessageReciepts;
import com.wifiwireless.model.Messages;

@Stateless
public class MessageRecieptsDao extends WifiDao implements Serializable,MessageRecieptsInterface{
	
	EntityManager em = null;

	/**
	 * Default constructor.
	 */
	
	public void addMesages(MessageReciepts reciepts) {
		em = getEm();

		try {
			em.getTransaction().begin();
			
				em.persist(reciepts);
			
			em.getTransaction().commit();
		} catch (Exception exception) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			

		} finally {

			em.close();
		}

	}
	public void mergeNumber(MessageReciepts reciepts) {
		em = getEm();

		try {
			
			em.getTransaction().begin();
			em.merge(reciepts);
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
