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


    public User getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
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
        public UserAuth getUserAuthByToken(String authorization)  {
          return userRepository.getUserAuthByAuthToken(authorization);

        }

        @Transactional(propagation = Propagation.REQUIRED)
        public UserAuth updateUserAuth(UserAuth userAuth) {
            UserAuth userAuth1 = userRepository.updateUserAuth(userAuth);
            return userAuth1;
        }


        public User getUserById(String uuid)  {
            User user = userRepository.getUserById(uuid);

            return user;
        }

        @Transactional(propagation = Propagation.REQUIRED)
        public void deleteUser(Integer id) {
            userRepository.deleteUser(id);
        }
    }

