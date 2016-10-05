package com.wifiwireless.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.NumberDetails;
@Stateless
public class NumberDetailsDao extends PL4BaseDAO implements NumberDetailsInterface,Serializable{
	
	EntityManager em = null;

	/**
	 * Default constructor.
	 */
	
	public void addNumberDetails(ArrayList<NumberDetails> arrContact) {
		em = getEm();

		try {
			em.getTransaction().begin();
			for (NumberDetails contact : arrContact) {
				em.persist(contact);
			}
			em.getTransaction().commit();
		} catch (Exception exception) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			

		} finally {

			em.close();
		}

	}

	

}
