package com.safarov.tech_app.service;

import com.safarov.tech_app.dto.request.AccountToAccountRequestDTO;
import com.safarov.tech_app.dto.response.AccountResponseDTO;
import com.safarov.tech_app.dto.response.CommonResponseDTO;
import com.safarov.tech_app.dto.response.Status;
import com.safarov.tech_app.dto.response.StatusCode;
import com.safarov.tech_app.entity.Account;
import com.safarov.tech_app.exception.*;
import com.safarov.tech_app.repositories.AccountRepository;
import com.safarov.tech_app.util.AccountCheckUtil;
import com.safarov.tech_app.util.CurrentUser;
import com.safarov.tech_app.util.DTOCheckUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferMoneyService {
    AccountRepository accountRepository;
    CurrentUser currentUser;
    DTOCheckUtil dtoCheckUtil;
    AccountCheckUtil accountCheckUtil;

    @Transactional
    public CommonResponseDTO<?> account2account(AccountToAccountRequestDTO requestDTO) {
        dtoCheckUtil.isValid(requestDTO);
        Integer debitAccountNo = requestDTO.getDebitAccountNo();
        Account debitAccount = getAccountByAccountNo(debitAccountNo);
        checkUserAccessAccount(debitAccount);
        BigDecimal amount = requestDTO.getAmount();
        accountCheckUtil.checkAmount(requestDTO.getAmount());
        Integer creditAccountNo = requestDTO.getCreditAccountNo();
        accountCheckUtil.checkEqualityAccounts(debitAccountNo, creditAccountNo);
        checkAccountStatus(debitAccount);
        checkDebitAccountBalance(debitAccount.getBalance(), amount);
        Account creditAccount = getAccountByAccountNo(creditAccountNo);
        checkAccountStatus(creditAccount);
        debitAccount.setBalance(debitAccount.getBalance().subtract(amount));
        creditAccount.setBalance(creditAccount.getBalance().add(amount));
        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("Transfer completed successfully")
                .build()).data(AccountResponseDTO.builder()
                .accountNo(debitAccountNo)
                .balance(debitAccount.getBalance())
                .currency(debitAccount.getCurrency())
                .isActive(debitAccount.getIsActive())
                .build()).build();
    }


    private Account getAccountByAccountNo(Integer accountNo) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNo(accountNo);
        if (optionalAccount.isEmpty()) {
            throw NotExistAccountException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.ACCOUNT_NOT_EXIST)
                            .message("The account " + accountNo + " is not exist.")
                            .build()).build()).build();
        }
        return optionalAccount.get();
    }

    public void checkAccountStatus(Account account) {
        if (!account.getIsActive())
            throw NotActiveAccountException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.NOT_ACTIVE_ACCOUNT)
                            .message("The account: " + account.getAccountNo() + " is not active.")
                            .build()).build()).build();
    }

    private void checkDebitAccountBalance(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0)
            throw InsufficientBalanceException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.INSUFFICIENT_BALANCE)
                            .message("Balance: " + balance + " is less than " + amount)
                            .build()).build()).build();
    }

    private void checkUserAccessAccount(Account debitAccount) {
        String currentUserPin = currentUser.getCurrentUser().getUsername();
        String accountUserPin = debitAccount.getUser().getPin();
        if (!currentUserPin.equals(accountUserPin))
            throw InvalidAccountOwnerException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.ACCOUNT_ACCESS_FORBIDDEN)
                            .message("The user: " + currentUserPin + " doesn't have " +
                                    "access to account " + debitAccount.getAccountNo() + ".")
                            .build()).build()).build();
    }
}
