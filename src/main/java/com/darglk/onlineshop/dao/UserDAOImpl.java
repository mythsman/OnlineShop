package com.darglk.onlineshop.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;


import com.darglk.onlineshop.model.User;

public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void saveUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(user);
	}
	
	@Override
	public User getUser(Long userId) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(User.class, userId);
	}
	
	@Override
	public void deleteUser(int userId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete from User where id=:userId");
		
		query.setParameter("userId", userId);
		query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getByUsername(String username) {
		Session session = sessionFactory.getCurrentSession();
		return Collections.checkedList(session.createCriteria(User.class)
			.add(Restrictions.eq("username", username)).list(), User.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getByEmail(String email) {
		Session session = sessionFactory.getCurrentSession();
		return Collections.checkedList(session.createCriteria(User.class)
				.add(Restrictions.eq("email", email))
				.list(), User.class);
	}
}
