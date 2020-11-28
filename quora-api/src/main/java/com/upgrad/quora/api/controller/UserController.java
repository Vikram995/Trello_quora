package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.JwtTokenProvider;
import com.upgrad.quora.service.business.PasswordCryptographyProvider;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;



    @RequestMapping(method = RequestMethod.POST, value = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public ResponseEntity<SignupUserResponse> signup(SignupUserRequest request) throws SignUpRestrictedException {

        User user = userService.getUserByName(request.getUserName());
        if(user != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }
        User userByEmail = userService.getUserByEmail(request.getEmailAddress());
        if(userByEmail != null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }
        User userEntity = new User();
        userEntity.setAboutMe(request.getAboutMe());
        userEntity.setContactNumber(request.getContactNumber());
        userEntity.setCountry(request.getCountry());
        userEntity.setDob(request.getDob());
        userEntity.setEmail(request.getEmailAddress());
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setUserName(request.getUserName());
        userEntity.setRole("non-admin");

        String encrypt[] = passwordCryptographyProvider.encrypt(request.getPassword());
        userEntity.setSalt(encrypt[0]);
        userEntity.setPassword(encrypt[1]);
        userEntity.setUuid(UUID.randomUUID().toString());
        User user1 = userService.createUser(userEntity);

        SignupUserResponse response = new SignupUserResponse();
        response.setId(user1.getUuid());
        response.setStatus("USER SUCCESSFULLY REGISTERED");

        ResponseEntity<SignupUserResponse> responseResponseEntity = new ResponseEntity<SignupUserResponse>(response, HttpStatus.CREATED);


        return responseResponseEntity;

    }



    @RequestMapping(method = RequestMethod.POST, value = "/user/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestParam(name = "authorization") String authorization) {

        String pass[] = authorization.split("Basic ");

        byte[] decode = Base64.getDecoder().decode(pass[1]);
        String decoded = new String(decode);

        String encrypt[] = decoded.split(":");
        String password = encrypt[1];



        User user = userService.getUserByName(encrypt[0]);

        String encrypt1 = PasswordCryptographyProvider.encrypt(password, user.getSalt());

        if(user.getPassword().equals(encrypt1)) {
            SigninResponse response = new SigninResponse();
            response.setId(user.getUuid());
            response.setMessage("Authenticated Successfully");
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(user.getPassword());


            String token = jwtTokenProvider.generateToken(user.getUuid(), ZonedDateTime.now(), ZonedDateTime.now().plusHours(8));

            UserAuth userAuth = new UserAuth();
            userAuth.setToken(token);
            userAuth.setUuid(user.getUuid());
            userAuth.setUser(user);
            userAuth.setExpiresAt(ZonedDateTime.now().plusHours(8));
            userAuth.setLoginAt(ZonedDateTime.now());
            userAuth.setLogoutAt(null);

                userService.createToken(userAuth);

            HttpHeaders headers = new HttpHeaders();
            headers.add("access-token", userAuth.getToken());
            ResponseEntity<SigninResponse> responseResponseEntity = new ResponseEntity<>(response, headers, HttpStatus.OK);
            return responseResponseEntity;
        }

        return null;


    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> logout(@RequestHeader(value = "authorization") String authorization) throws SignOutRestrictedException {
        UserAuth userAuth = userService.getUserByToken(authorization);
        if(userAuth == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
        userAuth.setLogoutAt(ZonedDateTime.now());
        userService.updateUserAuth(userAuth);

        SignoutResponse signoutResponse = new SignoutResponse();
        signoutResponse.setId(userAuth.getUuid());
        signoutResponse.setMessage("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.OK);

    }

    }
