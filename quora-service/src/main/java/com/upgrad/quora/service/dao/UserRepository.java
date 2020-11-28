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


    public User getUserByEmail(String email) {

        try {
            return entityManager.createNamedQuery("getUserByEmail", User.class).setParameter("email", email).getSingleResult();
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

    public UserAuth getUserByAuthToken(String authorization) {

        try {
            UserAuth userAuth = entityManager.createNamedQuery("getUserByToken", UserAuth.class).setParameter("token", authorization).getSingleResult();
            return userAuth;
        }
        catch (NoResultException noResultException) {
            return null;
        }
    }

    public UserAuth updateUserAuth(UserAuth userAuth) {
        entityManager.merge(userAuth);
        return userAuth;
    }

    public User getUserById(String uuid) {
        try {
            return entityManager.createNamedQuery("getUserById", User.class).setParameter("uuid", uuid).getSingleResult();
        }
        catch (NoResultException noResultException) {
            return null;
        }
    }

   /* public User updateUser(User user) {
        User merge = entityManager.merge(user);
        return merge;
    }*/

    public void deleteUser(Integer id) {
        entityManager.flush();
        entityManager.clear();
        User user = entityManager.find(User.class, id);
       // entityManager.getTransaction().begin();

        entityManager.remove(user);
        //entityManager.getTransaction().commit();
    }

}
