package com.safarov.tech_app.exception;

import com.safarov.tech_app.dto.response.CommonResponseDTO;
import com.safarov.tech_app.dto.response.Status;
import com.safarov.tech_app.dto.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> internalError(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(
                CommonResponseDTO.builder().status(Status.builder()
                        .statusCode(StatusCode.INTERNAL_ERROR)
                        .message("Internal Error")
                        .build()).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = InvalidDTOException.class)
    public ResponseEntity<?> invalidDTO(InvalidDTOException invalidDTOException) {
        return new ResponseEntity<>(invalidDTOException.getResponseDTO(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserAlreadyExistException.class)
    public ResponseEntity<?> userAlreadyExists(UserAlreadyExistException userAlreadyExistException) {
        return new ResponseEntity<>(userAlreadyExistException.getCommonResponseDTO(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AccountAlreadyExistException.class)
    public ResponseEntity<?> accountAlreadyExists(AccountAlreadyExistException accountAlreadyExistException) {
        return new ResponseEntity<>(accountAlreadyExistException.getCommonResponseDTO(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NoSuchUserExistException.class)
    public ResponseEntity<?> userNotExist(NoSuchUserExistException noSuchUserExistException) {
        return new ResponseEntity<>(noSuchUserExistException.getCommonResponseDTO(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BadInputCredentialsException.class)
    public ResponseEntity<?> badCredentials(BadInputCredentialsException badInputCredentialsException) {
        return new ResponseEntity<>(badInputCredentialsException.getCommonResponseDTO(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = NotActiveAccountException.class)
    public ResponseEntity<?> notActiveAccount(NotActiveAccountException notActiveAccountException) {
        return new ResponseEntity<>(notActiveAccountException.getCommonResponseDTO(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidAmountException.class)
    public ResponseEntity<?> invalidAmount(InvalidAmountException invalidAmountException) {
        return new ResponseEntity<>(invalidAmountException.getCommonResponseDTO(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EqualsAccountException.class)
    public ResponseEntity<?> equalsAccount(EqualsAccountException equalsAccountException) {
        return new ResponseEntity<>(equalsAccountException.getCommonResponseDTO(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InsufficientBalanceException.class)
    public ResponseEntity<?> insufficientBalance(InsufficientBalanceException insufficientBalanceException) {
        return new ResponseEntity<>(insufficientBalanceException.getCommonResponseDTO(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotExistAccountException.class)
    public ResponseEntity<?> notExistAccount(NotExistAccountException notExistAccountException) {
        return new ResponseEntity<>(notExistAccountException.getCommonResponseDTO(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidAccountOwnerException.class)
    public ResponseEntity<?> invalidOwner(InvalidAccountOwnerException invalidAccountOwnerException) {
        return new ResponseEntity<>(invalidAccountOwnerException.getCommonResponseDTO(), HttpStatus.FORBIDDEN);
    }
}