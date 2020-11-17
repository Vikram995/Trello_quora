package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserRepository;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.UserNotFoundException;
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


        @Transactional(propagation = Propagation.REQUIRED)
        public UserAuth getUserByToken(String authorization)  {
            UserAuth userAuth = userRepository.getUserByAuthToken(authorization);
            if(userAuth != null) {

                //userAuth.setLogoutAt(ZonedDateTime.now());
                return userAuth;
            }
            return null;
        }

        @Transactional(propagation = Propagation.REQUIRED)
        public UserAuth updateUserAuth(UserAuth userAuth) {
            UserAuth userAuth1 = userRepository.updateUserAuth(userAuth);
            return userAuth1;
        }


        public User getUserById(String uuid) throws UserNotFoundException {
            User user = userRepository.getUserById(uuid);

            return user;
        }

        @Transactional(propagation = Propagation.REQUIRED)
        public void deleteUser(Integer id) {
            //User merge = userRepository.updateUser(user);
            userRepository.deleteUser(id);
        }
    }

