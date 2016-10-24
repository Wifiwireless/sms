package com.wifiwireless.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.plaf.synth.SynthSeparatorUI;

import com.wifiwireless.interfaces.CustomerCheckDaoInterface;
import com.wifiwireless.model.CustomerCheck;
import com.wifiwireless.model.CustomerDetails;

@Stateless
public class CustomerCheckDao extends WifiDao implements Serializable,CustomerCheckDaoInterface {

	EntityManager em = null;

	/**
	 * Default constructor.
	 */
	public CustomerCheck getData() {
		em = getEm();
		CustomerCheck che=new CustomerCheck();
		//System.out.println("Username"+username+"   Password"+Passkey);
		try {

			String qlString = "FROM CustomerCheck";
			TypedQuery<CustomerCheck> query = em.createQuery(qlString,
					CustomerCheck.class);

			//ArrayList<CustomerCheck> check=(ArrayList<CustomerCheck>) query.getResultList();
		che=query.getResultList().get(0);
		
		
		System.out.println(che.getDatemodified());
			// if(query.getResultList()!=null && query.getResultList().size()>0)
			}
			
		 catch (Exception exception) {
			System.out.println(exception);
			//LOG.error(exception);
		} finally {

			em.close();
		}
		return che;

	}
	
	public CustomerCheck getextension() {
		em = getEm();
		CustomerCheck che=null;
		//System.out.println("Username"+username+"   Password"+Passkey);
		try {

			String qlString = "FROM CustomerCheck";
			TypedQuery<CustomerCheck> query = em.createQuery(qlString,
					CustomerCheck.class);

			ArrayList<CustomerCheck> check=(ArrayList<CustomerCheck>) query.getResultList();
			che=query.getResultList().get(0);
				
				System.out.println("check ");
		
			
			
			// if(query.getResultList()!=null && query.getResultList().size()>0)
			}
			
		 catch (Exception exception) {
			System.out.println(exception);
			//LOG.error(exception);
		} finally {

			em.close();
		}
		return che;

	}
	public void addCustomerCheck(CustomerCheck check) {
		em = getEm();
System.out.println("in add check");
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
		System.out.println("in update check");
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
