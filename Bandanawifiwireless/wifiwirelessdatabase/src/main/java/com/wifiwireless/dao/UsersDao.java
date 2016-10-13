package com.wifiwireless.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.wifiwireless.interfaces.UsersInterfaces;
import com.wifiwireless.model.Users;

@Stateless
public class UsersDao extends PL4BaseDAO implements UsersInterfaces {
	
	EntityManager em = null;

	public Boolean autentication(String ext,String value) {
		em = getEm();
		String name="secret";
		try {

			String qlString = "SELECT user FROM Users user  "
					+ "WHERE  user.ext=:ext and user.name=:name";

			TypedQuery<Users> query = em.createQuery(qlString,
					Users.class);

			query.setParameter("ext", ext);
			query.setParameter("name", name);
			if (query.getResultList().size() > 0){
				
				Users users=query.getSingleResult();
				if(users!=null && users.getValue().equals(value))
				{
					return true;
				}
				else
					return false;
			}
			
		} catch (Exception exception) {
			System.out.println(exception);
			//LOG.error(exception);
		} finally {

			em.close();
		}
		return false;

	}
}
