package com.wifiwireless.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.wifiwireless.interfaces.MessagesInterface;
import com.wifiwireless.model.Messages;
import com.wifiwireless.model.NumberDetails;
@Stateless
public class MessagesDao extends WifiDao implements MessagesInterface,Serializable{
	
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
	public ArrayList<Messages> getMessageByMsisdn(String destination) {
		em = getEm();
		ArrayList<Messages> replies=new ArrayList<Messages>();
		try {

			String qlString = "SELECT reply FROM Messages reply  "
					+ "WHERE  reply.destination=:destination";

			TypedQuery<Messages> query = em.createQuery(qlString,
					Messages.class);

			query.setParameter("destination", destination);
			if (query.getResultList().size() > 0){
				
				replies=(ArrayList<Messages>)query.getResultList();
				System.out.println("replies found "+replies.size());
				
			// if(query.getResultList()!=null && query.getResultList().size()>0)
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println(exception);
			//LOG.error(exception);
		} finally {

			em.close();
		}
		return replies;

	}
	
	

	

	

}
