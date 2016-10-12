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
	
	public String checkNumber(String username,String password) {
		em = getEm();
		
		try {

			String qlString = "SELECT number FROM NumberDetails number  "
					+ "WHERE  number.username=:username and number.password=:password";

			TypedQuery<NumberDetails> query = em.createQuery(qlString,
					NumberDetails.class);

			query.setParameter("username", username);
			query.setParameter("password", password);
			if (query.getResultList().size() > 0){
				
				NumberDetails numbers=query.getSingleResult();
				System.out.println(numbers.getPaidflag());
				if(numbers.getPaidflag())	
				{
					return numbers.getMsisdn();
					
				}
				else{
					return "";
				}
			// if(query.getResultList()!=null && query.getResultList().size()>0)
			}
			
		} catch (Exception exception) {
			//LOG.error(exception);
		} finally {

			em.close();
		}
		return "";

	}

	

	

}
