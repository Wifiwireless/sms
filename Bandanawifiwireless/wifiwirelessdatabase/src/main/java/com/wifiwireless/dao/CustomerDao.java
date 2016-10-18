package com.wifiwireless.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import com.wifiwireless.interfaces.CustomerDaoInterface;
import com.wifiwireless.model.CustomerDetails;
import com.wifiwireless.model.MessageReciepts;

@Stateless
public class CustomerDao extends WifiDao implements Serializable,CustomerDaoInterface {
	
	EntityManager em = null;

	/**
	 * Default constructor.
	 */
	
	public void addCustomer(ArrayList<CustomerDetails> customer) {
		em = getEm();

		try {
			em.getTransaction().begin();
			for(CustomerDetails cus:customer)
			{
				em.persist(cus);
			}
			
			em.getTransaction().commit();
		} catch (Exception exception) {
			exception.printStackTrace();
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			

		} finally {

			em.close();
		}

	}
	public void updateCustomer(ArrayList<CustomerDetails> customer) {
		em = getEm();

		try {
			
			em.getTransaction().begin();
			for(CustomerDetails cus:customer)
			em.merge(cus);
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
