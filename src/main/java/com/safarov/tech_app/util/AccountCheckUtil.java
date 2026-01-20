package com.safarov.tech_app.util;

import com.safarov.tech_app.dto.response.CommonResponseDTO;
import com.safarov.tech_app.dto.response.Status;
import com.safarov.tech_app.dto.response.StatusCode;
import com.safarov.tech_app.exception.EqualsAccountException;
import com.safarov.tech_app.exception.InvalidAmountException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class AccountCheckUtil {

    public void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw InvalidAmountException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.INVALID_AMOUNT)
                            .message("Amount must be greater than 0.")
                            .build()).build()).build();
        }
    }

    public void checkEqualityAccounts(Integer debitAccountNo, Integer creditAccountNo) {
        if (debitAccountNo.equals(creditAccountNo))
            throw EqualsAccountException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.EQUALS_ACCOUNT)
                            .message("Debit account: " + debitAccountNo
                                    + " and credit account " + creditAccountNo
                                    + " are same.")
                            .build()).build()).build();
    }

}
