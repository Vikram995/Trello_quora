package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @RequestMapping(method = RequestMethod.PUT, value = "/question/edit/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(@PathVariable(name = "questionId") String uuid, @RequestParam(name = "authorization") String authorization, QuestionEditRequest questionEditRequest) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuth userAuth = userService.getUserByToken(authorization);
        if(userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }
        Question question = questionService.getQuestionById(uuid);

        if(question == null) {
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
        }

        if(userAuth.getUser() != question.getUser()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
            question.setContent(questionEditRequest.getContent());

        Question updateQuestion = questionService.updateQuestion(question);

        QuestionEditResponse questionEditResponse = new QuestionEditResponse();
        questionEditResponse.setId(updateQuestion.getUuid());
        questionEditResponse.setStatus("QUESTION EDITED");

        return new ResponseEntity<>(questionEditResponse, HttpStatus.OK);


    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable(name = "questionId") String uuid, @RequestParam(name = "authorization") String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuth userAuth = userService.getUserByToken(authorization);
        if(userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
        }

        Question question = questionService.getQuestionById(uuid);

        if(question == null) {
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
        }

        if((userAuth.getUser() != question.getUser()) || userAuth.getUser().getRole().equals("non-admin")) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }

        questionService.deleteQuestion(question.getId());

        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse();
        questionDeleteResponse.setId(question.getUuid());
        questionDeleteResponse.setStatus("QUESTION DELETED");

        return new ResponseEntity<>(questionDeleteResponse, HttpStatus.OK);

    }



    @RequestMapping(method = RequestMethod.GET, value = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestionsByUser(@PathVariable(name = "userId") String uuid, @RequestParam(name = "authorization") String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuth userAuth = userService.getUserByToken(authorization);
        if(userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions posted by a specific user");
        }

        User user = userService.getUserById(uuid);
        if(user == null) {
            throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");
        }

        List<Question> allQuestionsByUser = questionService.getAllQuestionsByUser(user);

        QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
        String questions = "";
        String uuids = "";
        for (Question question : allQuestionsByUser) {
            questions += question.getContent() +", ";
            uuids += question.getUuid() +", ";
        }
        questionDetailsResponse.setContent(questions);
        questionDetailsResponse.setId(uuids);

        return new ResponseEntity<>(questionDetailsResponse, HttpStatus.OK);


    }
}
