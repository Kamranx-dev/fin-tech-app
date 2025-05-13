package com.safarov.techapp.service;

import com.safarov.techapp.dto.response.AccountResponseDTOList;
import com.safarov.techapp.dto.response.CommonResponseDTO;
import com.safarov.techapp.dto.response.Status;
import com.safarov.techapp.dto.response.StatusCode;
import com.safarov.techapp.entity.Account;
import com.safarov.techapp.entity.TechUser;
import com.safarov.techapp.exception.NoActiveAccountException;
import com.safarov.techapp.repositories.UserRepository;
import com.safarov.techapp.util.CurrentUser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
    UserRepository userRepository;
    CurrentUser currentUser;
    Logger logger;

    public CommonResponseDTO<?> getAccountsForUser() {
        TechUser user = userRepository.findUserByPin(currentUser.getCurrentUser().getUsername()).get();
        checkUserHasActiveAccount(user);

        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("Accounts for user:" + currentUser.getCurrentUser().getUsername() +
                        " was successfully fetched.")
                .build()).data(AccountResponseDTOList.entityToDto(getUserActiveAccounts(user))).build();
    }

    private void checkUserHasActiveAccount(TechUser user) {
        List<Account> activeAccountsList = getUserActiveAccounts(user);
        if (activeAccountsList.isEmpty()) {
            logger.error("User with pin : {} has not active account.", user.getPin());
            throw NoActiveAccountException.builder()
                    .commonResponseDTO(CommonResponseDTO.builder().status(Status.builder()
                            .statusCode(StatusCode.NO_ACTIVE_ACCOUNT)
                            .message("User with pin : " + user.getPin() +
                                    " has not active account.")
                            .build()).build()).build();
        }
    }

    private List<Account> getUserActiveAccounts(TechUser user) {
        return user.getAccountList().stream()
                .filter(Account::getIsActive).collect(Collectors.toList());
    }
}
