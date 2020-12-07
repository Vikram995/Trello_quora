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


    }
