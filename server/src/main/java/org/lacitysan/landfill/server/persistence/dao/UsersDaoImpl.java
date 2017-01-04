package org.lacitysan.landfill.server.persistence.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.lacitysan.landfill.server.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alvin Quach
 */
@SuppressWarnings("unchecked")
@Repository
public class UsersDaoImpl implements UsersDao {
	
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	@Override
	@Transactional
	public User getUserByUsername(String username) {
		String query = "from User where username=?";
		List<User> results = (List<User>)hibernateTemplate.find(query, username);
		if (!results.isEmpty()) {
			User result = results.get(0);
			Hibernate.initialize(result.getUserGroups());
			// TODO Initialize user roles.
			return result;
		}
		return null;
	}
	
}