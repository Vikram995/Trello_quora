package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepository {

    @PersistenceContext
    EntityManager entityManager;

    public User getUserByName(String username) {

        try {
            return entityManager.createNamedQuery("getUserByName", User.class).setParameter("username", username).getSingleResult();
        }
        catch (NoResultException noResultException) {
            return null;
        }

    }

    public User createUser(User user) {
        entityManager.persist(user);
        return user;
    }

    public void createToken(UserAuth auth) {
        entityManager.persist(auth);
    }

}
