package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionRepository {


    @PersistenceContext
    EntityManager entityManager;


    public void createQuestion(Question question) {

        entityManager.persist(question);
    }

    public List<Question> getAllQuestions() {

        List<Question> allQuestions = entityManager.createNamedQuery("getAllQuestions", Question.class).getResultList();
        return allQuestions;
    }


    public Question getQuestionById(String uuid) {
        try {
            return entityManager.createNamedQuery("getQuestionById", Question.class).setParameter("uuid", uuid).getSingleResult();
        }
        catch (NoResultException noResultException) {
            return null;
        }
    }

    public Question updateQuestion(Question question) {

        entityManager.merge(question);
        return question;

    }

    public void deleteQuestion(Integer id) {
        entityManager.flush();
        entityManager.clear();
        Question question = entityManager.find(Question.class, id);
        entityManager.remove(question);
    }

    public List<Question> getAllQuestionsByUser(User user) {

        try {
        return entityManager.createNamedQuery("getQuestionsByUser", Question.class).setParameter("user", user).getResultList();
    }
        catch (NoResultException noResultException) {
            return null;
        }
    }
}
