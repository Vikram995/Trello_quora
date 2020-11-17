package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserRepository;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getUserByName(String username) {
        User user = userRepository.getUserByName(username);
       return user;
        }


    @Transactional(propagation = Propagation.REQUIRED)
        public User createUser(User user) {
            userRepository.createUser(user);
            return user;
        }


        @Transactional(propagation = Propagation.REQUIRED)
    public void createToken(UserAuth userAuth) {
        userRepository.createToken(userAuth);
        }
    }

