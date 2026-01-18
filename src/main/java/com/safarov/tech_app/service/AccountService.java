package com.safarov.tech_app.service;

import com.safarov.tech_app.dto.response.*;
import com.safarov.tech_app.entity.Account;
import com.safarov.tech_app.entity.TechUser;
import com.safarov.tech_app.exception.NotActiveAccountException;
import com.safarov.tech_app.repositories.TechUserRepository;
import com.safarov.tech_app.util.CurrentUser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
    CurrentUser currentUser;
    TechUserRepository techUserRepository;

    public CommonResponseDTO<?> getAccountsForUser() {
        TechUser user = techUserRepository.findByPin(currentUser.getCurrentUser().getUsername()).get();

        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("Account(s) for user: "
                        + user.getName()
                        + " was successfully fetched.")
                .build()).data(getActiveAccountDTOList(user)).build();
    }


    private AccountResponseDTOList getActiveAccountDTOList(TechUser user) {
        List<Account> activeAccountList = user.getAccountList()
                .stream()
                .filter(Account::getIsActive)
                .collect(Collectors.toList());

        if (activeAccountList.isEmpty())
            throw NotActiveAccountException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.NOT_ACTIVE_ACCOUNT)
                            .message("There is no active account.").build()).build()).build();

        return AccountResponseDTOList.entityToDto(activeAccountList);
    }

}
