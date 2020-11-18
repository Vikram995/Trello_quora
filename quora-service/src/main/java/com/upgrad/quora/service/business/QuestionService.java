package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionRepository;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;


    @Transactional(propagation = Propagation.REQUIRED)
    public void createQuestion(Question question) {
        questionRepository.createQuestion(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.getAllQuestions();
    }

    public Question getQuestionById(String uuid) {
        Question question = questionRepository.getQuestionById(uuid);

        return question;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Question updateQuestion(Question question) {

        return questionRepository.updateQuestion(question);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteQuestion(Integer id) {

        questionRepository.deleteQuestion(id);
    }

    public List<Question> getAllQuestionsByUser(User user) {
        return questionRepository.getAllQuestionsByUser(user);
    }
}
