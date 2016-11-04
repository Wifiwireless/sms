package com.wifiwireless.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wifiwireless.interfaces.MessagesInterface;
import com.wifiwireless.model.Messages;

@Stateless
public class MessagesDao extends WifiDao implements MessagesInterface,Serializable{
	
	private static final Logger log = LoggerFactory.getLogger(MessagesDao.class);
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
					+ "WHERE  reply.destination=:destination AND reply.readOut =:readOut";

			TypedQuery<Messages> query = em.createQuery(qlString,
					Messages.class);

			query.setParameter("destination", destination);
			query.setParameter("readOut", false);
			
			
			if (query.getResultList().size() > 0){
				
				replies=(ArrayList<Messages>)query.getResultList();
				log.info("replies found "+replies.size());
				
			// if(query.getResultList()!=null && query.getResultList().size()>0)
			}else{
				log.info("replies found "+replies.size());
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		
			//LOG.error(exception);
		} finally {

			em.close();
		}
		return replies;

	}
	public void updateMesages(Messages messages) {
		// TODO Auto-generated method stub
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
