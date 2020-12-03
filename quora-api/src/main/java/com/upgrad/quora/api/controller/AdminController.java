package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.UserDeleteResponse;
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
public class AdminController {

    @Autowired
    UserService userService;

    //deletes the user only if the user is an admin
    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable(name = "userId") String uuid, @RequestHeader(name = "authorization") String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuth userAuth = userService.getUserAuthByToken(authorization);
        if(userAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");

        }
        if(userAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        }

        User user = userAuth.getUser();
        if(!(user.getRole().equals("admin"))) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");

        }
        User userById = userService.getUserById(uuid);
        if(userById == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
        }

        userService.deleteUser(userById.getId());

        UserDeleteResponse userDeleteResponse = new UserDeleteResponse();
        userDeleteResponse.setId(uuid);
        userDeleteResponse.setStatus("USER SUCCESSFULLY DELETED");

        return new ResponseEntity<>(userDeleteResponse, HttpStatus.OK);




    }
}
