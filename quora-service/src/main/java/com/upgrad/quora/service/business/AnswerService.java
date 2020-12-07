package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.AnswerRepository;
import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {

    @Autowired
    AnswerRepository answerRepository;

//Creates an answer
    @Transactional(propagation = Propagation.REQUIRED)
    public Answer createAnswer(Answer answer) {

        answerRepository.createAnswer(answer);
        return answer;
    }

//Returns an answer by ID
    public Answer getAnswerById(String auuid) {
        return answerRepository.getAnswerById(auuid);
    }

//Updates Answer
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAnswer(Answer answer) {
        answerRepository.updateAnswer(answer);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAnswer(Integer id) {
        answerRepository.deleteAnswer(id);
    }


    public List<Answer> getAllAnswersToQuestion(Question question) {
        return answerRepository.getAllAnswersToQuestion(question);
    }
}
