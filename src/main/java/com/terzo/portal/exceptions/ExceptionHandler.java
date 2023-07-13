package com.terzo.portal.exceptions;

import com.terzo.portal.handler.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> invalidCredentialsException(){
        return ResponseHandler.generateResponse("Invalid credentials!!!", HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(LeaveTypeNotAvailableException.class)
    public ResponseEntity<Object> leaveTypeNotAvailableException(){
        return ResponseHandler.generateResponse("You have already used all the days in this type of leave",HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> userNotFoundException(){
        return ResponseHandler.generateResponse("User not found with the given mail id",HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<Object> userNotVerifiedException(){
        return ResponseHandler.generateResponse("User is not verified with OTP",HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> expiredJwtException(){
        return ResponseHandler.generateResponse("Invalid JWT Token",HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AllFieldsRequiredException.class)
    public ResponseEntity<Object> allFieldsRequiredException(){
        return ResponseHandler.generateResponse("From date is after To date",HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(SelfDeletionException.class)
    public ResponseEntity<Object> selfDeletionException(){
        return ResponseHandler.generateResponse("You cannot delete your account while you are logged in",HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserWithThisEmailAlreadyExistsException.class)
    public ResponseEntity<Object> userWithThisEmailAlreadyExistsException(){
        return ResponseHandler.generateResponse("Try with different email",HttpStatus.CONFLICT);
    }
}
