package com.wifiwireless.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import com.wifiwireless.interfaces.CustomerCheckDaoInterface;
import com.wifiwireless.model.CustomerCheck;
import com.wifiwireless.model.CustomerDetails;

@Stateless
public class CustomerCheckDao extends WifiDao implements Serializable,CustomerCheckDaoInterface {

	EntityManager em = null;

	/**
	 * Default constructor.
	 */
	
	public void addCustomerCheck(CustomerCheck check) {
		em = getEm();

		try {
			em.getTransaction().begin();
			
				em.persist(check);
			
			em.getTransaction().commit();
		} catch (Exception exception) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			

		} finally {

			em.close();
		}

	}
	public void updateCustomerCheck(CustomerCheck check) {
		em = getEm();

		try {
			
			em.getTransaction().begin();
			
			em.merge(check);
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
