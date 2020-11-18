package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
public class QuestionController {
    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(method = RequestMethod.POST, value = "/question/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(QuestionRequest request, @RequestParam(name = "authorization") String authorization) throws AuthorizationFailedException {
        UserAuth userAuth = userService.getUserByToken(authorization);
        if(userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }

        Question question = new Question();
        question.setContent(request.getContent());
        question.setDate(ZonedDateTime.now());
        question.setUuid(userAuth.getUuid());
        question.setUser(userAuth.getUser());

        questionService.createQuestion(question);

        QuestionResponse questionResponse = new QuestionResponse();

        questionResponse.setId(userAuth.getUuid());
        questionResponse.setStatus("QUESTION CREATED");

        return new ResponseEntity<>(questionResponse, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestions(@RequestParam(name = "authorization") String authorization) throws AuthorizationFailedException {
        UserAuth userAuth = userService.getUserByToken(authorization);
        if(userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }
        List<Question> questions = questionService.getAllQuestions();


        QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
        //questionDetailsResponse.setId(userAuth.getUuid());
        String allQuestions = "";
        String id = "";
        for (Question q: questions) {
            allQuestions += q.getContent() + ", ";
            id += q.getUuid() + ", ";
        }
        questionDetailsResponse.setContent(allQuestions);
        questionDetailsResponse.setId(id);

        return new ResponseEntity<>(questionDetailsResponse, HttpStatus.OK);

    }
}
