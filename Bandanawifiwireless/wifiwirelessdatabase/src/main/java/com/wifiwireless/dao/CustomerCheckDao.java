package com.wifiwireless.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wifiwireless.interfaces.CustomerCheckDaoInterface;
import com.wifiwireless.model.CustomerCheck;


@Stateless
public class CustomerCheckDao extends WifiDao implements Serializable, CustomerCheckDaoInterface {

	private static final Logger log = LoggerFactory.getLogger(CustomerCheckDao.class);
	
	EntityManager em = null;

	/**
	 * Default constructor.
	 */
	public CustomerCheck getData() {
		em = getEm();
		CustomerCheck che = new CustomerCheck();
		// log.info("Username"+username+" Password"+Passkey);
		try {

			String qlString = "FROM CustomerCheck";
			TypedQuery<CustomerCheck> query = em.createQuery(qlString, CustomerCheck.class);

			// ArrayList<CustomerCheck> check=(ArrayList<CustomerCheck>)
			// query.getResultList();
			che = query.getResultList().get(0);

			// if(query.getResultList()!=null && query.getResultList().size()>0)
		}

		catch (Exception exception) {
			log.info(""+exception);
			// LOG.error(exception);
		} finally {

			em.close();
		}
		return che;

	}

	public CustomerCheck getextension() {
		em = getEm();
		CustomerCheck che = null;
		// log.info("Username"+username+" Password"+Passkey);
		try {

			String qlString = "FROM CustomerCheck";
			TypedQuery<CustomerCheck> query = em.createQuery(qlString, CustomerCheck.class);

			ArrayList<CustomerCheck> check = (ArrayList<CustomerCheck>) query.getResultList();
			che = query.getResultList().get(0);

			log.info("check ");

			// if(query.getResultList()!=null && query.getResultList().size()>0)
		}

		catch (Exception exception) {
			log.info(""+exception);
			// LOG.error(exception);
		} finally {

			em.close();
		}
		return che;

	}

	public void addCustomerCheck(CustomerCheck check) {
		em = getEm();
		log.info("in add check");
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
		log.info("in update check");
		try {

			em.getTransaction().begin();

			em.merge(check);
			em.getTransaction().commit();

		} catch (Exception exception) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			// LOG.error(exception.getMessage());

		} finally {

			em.close();
		}

	}
}
