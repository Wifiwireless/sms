package com.wifiwireless.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.NumberDetails;
@Stateless
public class NumberDetailsDao extends WifiDao implements NumberDetailsInterface,Serializable{
	
	private static final Logger log = LoggerFactory.getLogger(NumberDetailsDao.class);
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
		log.info("email"+username+"   Password"+Passkey);
		try {

			String qlString = "SELECT number FROM NumberDetails number  "
					+ "WHERE  number.username=:username and number.password=:password";

			log.info("------------------------------------------------");
			TypedQuery<NumberDetails> query = em.createQuery(qlString,
					NumberDetails.class);

			query.setParameter("username", username);
			query.setParameter("password", Passkey);
			if (query.getResultList().size() > 0){
				
				NumberDetails numbers=query.getSingleResult();
				log.info(""+numbers.getPaidflag());
				if(numbers.getPaidflag())	
				{
					log.info("in true got number  ");
					return numbers.getMsisdn();
					
				}
				else{
					
					return "";
				}
			// if(query.getResultList()!=null && query.getResultList().size()>0)
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
			//LOG.error(exception);
		} finally {

			em.close();
		}
		return "";

	}
	
	
	public NumberDetails getNumberDetails(String username,String Passkey) {
		em = getEm();
		NumberDetails numbers=new NumberDetails();
		log.info("email"+username+"   Password"+Passkey);
		try {

			String qlString = "SELECT number FROM NumberDetails number  "
					+ "WHERE  number.username=:username and number.password=:password";

			TypedQuery<NumberDetails> query = em.createQuery(qlString,
					NumberDetails.class);

			query.setParameter("username", username);
			query.setParameter("password", Passkey);
			if (query.getResultList().size() > 0){
				
				numbers=query.getSingleResult();
				log.info(""+numbers.getPaidflag());
				
			// if(query.getResultList()!=null && query.getResultList().size()>0)
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
			//LOG.error(exception);
		} finally {

			em.close();
		}
		return numbers;

	}
	
	
	public NumberDetails getNumberDetailsByMsisdn(String msisdn) {
		em = getEm();
		NumberDetails numbers=new NumberDetails();
		try {

			String qlString = "SELECT number FROM NumberDetails number  "
					+ "WHERE  number.msisdn=:msisdn";

			TypedQuery<NumberDetails> query = em.createQuery(qlString,
					NumberDetails.class);

			query.setParameter("msisdn", msisdn);
			if (query.getResultList().size() > 0){
				
				numbers=query.getSingleResult();
				log.info(numbers.getUsername());
				
			// if(query.getResultList()!=null && query.getResultList().size()>0)
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {

			em.close();
		}
		return numbers;

	}
	public Boolean checkandUpdatePaidFlag(String msisdn,String username,String password) {
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
	public List<NumberDetails> getNumberDetailsByPaidFlag(boolean paidFlag) {
		em = getEm();
		List<NumberDetails> numbers = null;
		try {

			String qlString = "SELECT number FROM NumberDetails number  "
					+ "WHERE  number.paidflag=:paidflag";

			TypedQuery<NumberDetails> query = em.createQuery(qlString,
					NumberDetails.class);

			query.setParameter("paidflag", paidFlag);
			if (query.getResultList() != null){
				
				numbers = query.getResultList();
				
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
			//LOG.error(exception);
		} finally {

			em.close();
		}
		return numbers;
	}

	

}
