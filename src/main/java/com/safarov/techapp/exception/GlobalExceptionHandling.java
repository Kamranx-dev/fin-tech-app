package com.safarov.techapp.exception;

import com.safarov.techapp.dto.response.CommonResponseDTO;
import com.safarov.techapp.dto.response.Status;
import com.safarov.techapp.dto.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> internalError(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>
                (
                        CommonResponseDTO.builder().status(Status.builder()
                                .statusCode(StatusCode.INTERNAL_ERROR)
                                .message("Internal Error")
                                .build()).build(), HttpStatus.INTERNAL_SERVER_ERROR
                );
    }

    @ExceptionHandler(value = InvalidDTOException.class)
    public ResponseEntity<?> invalidDTO(InvalidDTOException invalidDTOException) {
        return new ResponseEntity<>(invalidDTOException.getResponseDTO(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserAlreadyExistException.class)
    public ResponseEntity<?> userExists(UserAlreadyExistException userAlreadyExistException) {
        return new ResponseEntity<>(userAlreadyExistException.getCommonResponseDTO(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AccountAlreadyExist.class)
    public ResponseEntity<?> accountExist(AccountAlreadyExist accountAlreadyExist) {
        return new ResponseEntity<>(accountAlreadyExist.getCommonResponseDTO(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NoSuchUserExistException.class)
    public ResponseEntity<?> userNotExist(NoSuchUserExistException noSuchUserExistException) {
        return new ResponseEntity<>(noSuchUserExistException.getResponseDTO(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BadInputCredentialException.class)
    public ResponseEntity<?> badCredentials(BadInputCredentialException badInputCredentialException) {
        return new ResponseEntity<>(badInputCredentialException.getResponseDTO(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = NoActiveAccountException.class)
    public ResponseEntity<?> noActiveAccount(NoActiveAccountException noActiveAccountException) {
        return new ResponseEntity<>(noActiveAccountException.getCommonResponseDTO(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidAmountException.class)
    public ResponseEntity<?> invalidAmount(InvalidAmountException invalidAmountException) {
        return new ResponseEntity<>(invalidAmountException.getCommonResponseDTO(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EqualsAccountException.class)
    public ResponseEntity<?> sameAccounts(EqualsAccountException equalsAccountException) {
        return new ResponseEntity<>(equalsAccountException.getCommonResponseDTO(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InsufficientBalanceException.class)
    public ResponseEntity<?> insufficientBalance(InsufficientBalanceException insufficientBalanceException) {
        return new ResponseEntity<>(insufficientBalanceException.getCommonResponseDTO(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoExistAccountException.class)
    public ResponseEntity<?> noExistAccount(NoExistAccountException noExistAccountException) {
        return new ResponseEntity<>(noExistAccountException.getCommonResponseDTO(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidUserAccessException.class)
    public ResponseEntity<?> userNotAccess(InvalidUserAccessException invalidUserAccessException) {
        return new ResponseEntity<>(invalidUserAccessException.getCommonResponseDTO(), HttpStatus.FORBIDDEN);
    }
}
