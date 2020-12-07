package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.entity.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerRepository {


    @PersistenceContext
    EntityManager entityManager;
    public Answer createAnswer(Answer answer) {

        entityManager.persist(answer);
        return answer;
    }

    public Answer getAnswerById(String uuid) {
        try{

        Answer answer = entityManager.createNamedQuery("getAnswerById", Answer.class).setParameter("uuid", uuid).getSingleResult();
        return answer;

        }catch (NoResultException noResultException) {
            return null;
        }

    }

    public void updateAnswer(Answer answer) {
        entityManager.merge(answer);
    }

    public void deleteAnswer(Integer id) {
        entityManager.flush();
        entityManager.clear();
        Answer answer = entityManager.find(Answer.class, id);
        entityManager.remove(answer);
    }


    public List<Answer> getAllAnswersToQuestion(Question question) {
        try {
            return entityManager.createNamedQuery("getAllQuestionsByQuestion", Answer.class).setParameter("question", question).getResultList();
        }
        catch (NoResultException noResultException) {
            return null;
        }
    }
}
