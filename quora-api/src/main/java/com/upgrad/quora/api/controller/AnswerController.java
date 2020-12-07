package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class AnswerController {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    AnswerService answerService;

    //creates an answer to a particular question
    //throws Invalid Question Exception and Authorization Failed Exception

    @RequestMapping(method = RequestMethod.POST, value = "/question/{questionId}/answer/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable(name = "questionId") String quuid, @RequestHeader(name = "authorization")
            String authorization, AnswerRequest answerRequest) throws InvalidQuestionException, AuthorizationFailedException {
        Question question = questionService.getQuestionById(quuid);
        if(question == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }

        UserAuth userAuth = userService.getUserAuthByToken(authorization);
        if(userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
        }

        Answer answer = new Answer();
        answer.setAns(answerRequest.getAnswer());
        answer.setDate(ZonedDateTime.now());
        answer.setQuestion(question);
        answer.setUser(userAuth.getUser());
        answer.setUuid(UUID.randomUUID().toString());

        Answer answer1 = answerService.createAnswer(answer);
        AnswerResponse answerResponse = new AnswerResponse();
        answerResponse.setId(answer1.getUuid());
        answerResponse.setStatus("ANSWER CREATED");

        return new ResponseEntity<>(answerResponse, HttpStatus.CREATED);


    }


    //updates an answer
    @RequestMapping(method = RequestMethod.PUT, value =  "/answer/edit/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(@PathVariable(name = "answerId") String auuid,
                                                                AnswerEditRequest answerEditRequest, @RequestHeader(name = "authorization")
                                                                            String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuth userAuth = userService.getUserAuthByToken(authorization);
        if(userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an answer");
        }

        Answer answer = answerService.getAnswerById(auuid);
        if(answer == null) {
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }

        if(userAuth.getUser() != answer.getUser()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }

        answer.setAns(answerEditRequest.getContent());

        answerService.updateAnswer(answer);

        AnswerEditResponse answerEditResponse = new AnswerEditResponse();
        answerEditResponse.setId(auuid);
        answerEditResponse.setStatus("ANSWER EDITED");

        return new ResponseEntity<>(answerEditResponse, HttpStatus.OK);


    }

    //deletes an answer only if created by the same particular user
    @RequestMapping(method = RequestMethod.DELETE, value = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable(name = "answerId") String auuid, @RequestHeader(name = "authorization") String authorization) throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuth userAuth = userService.getUserAuthByToken(authorization);
        if(userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete an answer");
        }

        Answer answer = answerService.getAnswerById(auuid);
        if(answer == null) {
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }

        if(userAuth.getUser() != answer.getUser() || userAuth.getUser().getRole().equals("non-admin")) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }


        answerService.deleteAnswer(answer.getId());

        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse();
        answerDeleteResponse.setId(auuid);
        answerDeleteResponse.setStatus("ANSWER DELETED");

        return new ResponseEntity<>(answerDeleteResponse, HttpStatus.OK);


    }


//fetches all answers for a particular question
    @RequestMapping(method = RequestMethod.GET, value = "answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDetailsResponse> getAllAnswersToQuestion(@PathVariable(name = "questionId") String quuid, @RequestHeader(name = "authorization") String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuth userAuth = userService.getUserAuthByToken(authorization);
        if(userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get the answers");
        }

        Question question = questionService.getQuestionById(quuid);
        if(question == null) {
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }

        List<Answer> list = answerService.getAllAnswersToQuestion(question);

        AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse();
        answerDetailsResponse.setQuestionContent(question.getContent());


        String answers = "";
        String uuids = "";

        for (Answer a : list) {
            answers += a.getAns() + ", ";
            uuids += a.getUuid() + ", ";
        }

        answerDetailsResponse.setAnswerContent(answers);
        answerDetailsResponse.setId(uuids);

        return new ResponseEntity<>(answerDetailsResponse, HttpStatus.OK);

    }
}
