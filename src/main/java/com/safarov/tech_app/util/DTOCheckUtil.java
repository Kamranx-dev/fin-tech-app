package com.safarov.tech_app.util;

import com.safarov.tech_app.dto.request.AccountRequestDTO;
import com.safarov.tech_app.dto.request.AccountToAccountRequestDTO;
import com.safarov.tech_app.dto.request.AuthenticationRequestDTO;
import com.safarov.tech_app.dto.request.UserRequestDTO;
import com.safarov.tech_app.dto.response.CommonResponseDTO;
import com.safarov.tech_app.dto.response.Status;
import com.safarov.tech_app.dto.response.StatusCode;
import com.safarov.tech_app.exception.EqualsAccountException;
import com.safarov.tech_app.exception.EqualsAccountNoException;
import com.safarov.tech_app.exception.InvalidDTOException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DTOCheckUtil {
    Logger logger;

    public void isValid(UserRequestDTO userRequestDTO) {
        checkDtoInput(userRequestDTO.getName());
        checkDtoInput(userRequestDTO.getSurname());
        checkDtoInput(userRequestDTO.getPin());
        checkDtoInput(userRequestDTO.getPassword());

        List<AccountRequestDTO> accountRequestDTOList = userRequestDTO.getAccountRequestDTOList();
        checkDtoInput(accountRequestDTOList);
        checkSameAccountsNo(accountRequestDTOList);
        accountRequestDTOList.forEach(account -> {
            checkDtoInput(account.getBalance());
            checkDtoInput(account.getCurrency());
            checkDtoInput(account.getIsActive());
            checkDtoInput(account.getAccountNo());
        });

    }

    public void isValid(AuthenticationRequestDTO authenticationRequestDTO) {
        checkDtoInput(authenticationRequestDTO.getPin());
        checkDtoInput(authenticationRequestDTO.getPassword());
    }

    public void isValid(AccountToAccountRequestDTO accountToAccountRequestDTO) {
        checkDtoInput(accountToAccountRequestDTO.getCreditAccountNo());
        checkDtoInput(accountToAccountRequestDTO.getDebitAccountNo());
        checkDtoInput(accountToAccountRequestDTO.getAmount());
    }

    private <T> void checkDtoInput(T t) {
        if (Objects.isNull(t) || t.toString().isBlank()
                || t instanceof Collection && ((Collection<?>) t).isEmpty()) {
            logger.error("DTO input is null or empty");
            throw InvalidDTOException.builder().responseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.INVALID_DTO)
                            .message("DTO input is null or empty")
                            .build()).build()).build();
        }
    }

    private void checkSameAccountsNo(List<AccountRequestDTO> accountRequestDTOList) {
        Set<Integer> accountNoSet = accountRequestDTOList
                .stream()
                .map(AccountRequestDTO::getAccountNo)
                .collect(Collectors.toSet());

        if (accountNoSet.size() != accountRequestDTOList.size())
            throw EqualsAccountNoException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.EQUALS_ACCOUNT_NO)
                            .message("Duplicate account numbers found in the request list.")
                            .build()).build()).build();
    }

}
