package com.upgrad.quora.api.controller;


import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.business.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {
    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;


}
