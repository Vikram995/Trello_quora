package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommonController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getProfile(@PathVariable(name = "userId") String uuid, @RequestHeader(name = "authorization") String authorization) throws UserNotFoundException, AuthorizationFailedException {
        UserAuth userAuth = userService.getUserAuthByToken(authorization);
        if(userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuth.getLogoutAt() != null) {

            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }
        User user = userService.getUserById(uuid);
        if(user == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
        userDetailsResponse.setFirstName(user.getFirstName());
        userDetailsResponse.setAboutMe(user.getAboutMe());
        userDetailsResponse.setContactNumber(user.getContactNumber());
        userDetailsResponse.setCountry(user.getCountry());
        userDetailsResponse.setDob(user.getDob());
        userDetailsResponse.setEmailAddress(user.getEmail());
        userDetailsResponse.setLastName(user.getLastName());
        userDetailsResponse.setUserName(user.getUserName());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }

    }

