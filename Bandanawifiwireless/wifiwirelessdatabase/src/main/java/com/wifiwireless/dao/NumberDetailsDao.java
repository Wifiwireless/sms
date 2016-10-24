package com.wifiwireless.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.NumberDetails;
@Stateless
public class NumberDetailsDao extends WifiDao implements NumberDetailsInterface,Serializable{
	
	EntityManager em = null;

	/**
	 * Default constructor.
	 */
	
	public void addNumberDetails(NumberDetails numberdetails) {
		em = getEm();

		try {
			em.getTransaction().begin();
			
				em.persist(numberdetails);
			
			em.getTransaction().commit();
		} catch (Exception exception) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			

		} finally {

			em.close();
		}

	}
	public void mergeNumber(NumberDetails number) {
		em = getEm();

		try {
			
			em.getTransaction().begin();
			em.merge(number);
			em.getTransaction().commit();
		
		} catch (Exception exception) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			//LOG.error(exception.getMessage());

		} finally {

			em.close();
		}

	}
	public String checkNumber(String username,String Passkey) {
		em = getEm();
		System.out.println("email"+username+"   Password"+Passkey);
		try {

			String qlString = "SELECT number FROM NumberDetails number  "
					+ "WHERE  number.username=:username and number.password=:password";

			System.out.println("------------------------------------------------");
			TypedQuery<NumberDetails> query = em.createQuery(qlString,
					NumberDetails.class);

			query.setParameter("username", username);
			query.setParameter("password", Passkey);
			if (query.getResultList().size() > 0){
				
				NumberDetails numbers=query.getSingleResult();
				System.out.println(numbers.getPaidflag());
				if(numbers.getPaidflag())	
				{
					System.out.println("in true got number  ");
					return numbers.getMsisdn();
					
				}
				else{
					
					return "";
				}
			// if(query.getResultList()!=null && query.getResultList().size()>0)
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println(exception);
			//LOG.error(exception);
		} finally {

			em.close();
		}
		return "";

	}
	
	
	public NumberDetails getNumberDetails(String username,String Passkey) {
		em = getEm();
		NumberDetails numbers=new NumberDetails();
		System.out.println("email"+username+"   Password"+Passkey);
		try {

			String qlString = "SELECT number FROM NumberDetails number  "
					+ "WHERE  number.username=:username and number.password=:password";

			TypedQuery<NumberDetails> query = em.createQuery(qlString,
					NumberDetails.class);

			query.setParameter("username", username);
			query.setParameter("password", Passkey);
			if (query.getResultList().size() > 0){
				
				numbers=query.getSingleResult();
				System.out.println(numbers.getPaidflag());
				
			// if(query.getResultList()!=null && query.getResultList().size()>0)
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println(exception);
			//LOG.error(exception);
		} finally {

			em.close();
		}
		return numbers;

	}
	public Boolean checkandUpdate(String msisdn,String username,String password) {
		em = getEm();
		
		try {

			String qlString = "SELECT number FROM NumberDetails number  "
					+ "WHERE  number.msisdn=:msisdn and number.username=:username and number.password=:password";

			TypedQuery<NumberDetails> query = em.createQuery(qlString,
					NumberDetails.class);
//			query.setParameter("sysadminId", sysadminId);
			query.setParameter("msisdn", msisdn);
			query.setParameter("username", username);
			query.setParameter("password", password);
			if (query.getResultList().size() > 0){
				
				NumberDetails numbers=query.getSingleResult();
				numbers.setPaidflag(true);
				em.getTransaction().begin();
				em.merge(numbers);
				em.getTransaction().commit();				
			return true;
			// if(query.getResultList()!=null && query.getResultList().size()>0)
			}
			
		} catch (Exception exception) {
			//LOG.error(exception);
		} finally {

			em.close();
		}
		return false;

	}

	

}
