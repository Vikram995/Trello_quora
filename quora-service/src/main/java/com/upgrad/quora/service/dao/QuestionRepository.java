package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
}
