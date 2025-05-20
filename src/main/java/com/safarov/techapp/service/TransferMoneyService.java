package com.safarov.techapp.service;

import com.safarov.techapp.dto.request.AccountToAccountRequestDTO;
import com.safarov.techapp.dto.response.AccountResponseDTO;
import com.safarov.techapp.dto.response.CommonResponseDTO;
import com.safarov.techapp.dto.response.Status;
import com.safarov.techapp.dto.response.StatusCode;
import com.safarov.techapp.entity.Account;
import com.safarov.techapp.exception.*;
import com.safarov.techapp.repositories.AccountRepository;
import com.safarov.techapp.util.CurrentUser;
import com.safarov.techapp.util.DTOCheckUtil;
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
    DTOCheckUtil dtoCheckUtil;
    AccountRepository accountRepository;
    CurrentUser currentUser;

    @Transactional
    public CommonResponseDTO<?> account2account(AccountToAccountRequestDTO requestDTO) {
        dtoCheckUtil.isValid(requestDTO);
        BigDecimal amount = requestDTO.getAmount();
        checkAmountPositive(amount);
        Account debitAccount = getAccountIfPresent(requestDTO.getDebitAccountNo());
        checkTokenUser(debitAccount);

        Account creditAccount = getAccountIfPresent(requestDTO.getCreditAccountNo());
        checkAccountsAreSame(debitAccount.getAccountNo(), creditAccount.getAccountNo());
        checkAccountIsActive(debitAccount);
        checkAccountIsActive(creditAccount);
        checkBalance(debitAccount.getBalance(), amount);

        return transferMoney(debitAccount, creditAccount, amount);
    }


    private void checkAmountPositive(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw InvalidAmountException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.INVALID_AMOUNT)
                            .message("Amount must be greater than 0")
                            .build()).build()).build();
        }
    }

    private void checkAccountsAreSame(Integer debitAccountNo, Integer creditAccountNo) {
        if (debitAccountNo.equals(creditAccountNo)) {
            throw EqualsAccountException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.EQUALS_ACCOUNTS)
                            .message("Debit account: " + debitAccountNo +
                                    " credit account: " + creditAccountNo +
                                    " are the same.")
                            .build()).build()).build();
        }
    }

    private Account getAccountIfPresent(Integer accountNo) {
        Optional<Account> byAccountNo = accountRepository.findByAccountNo(accountNo);
        if (byAccountNo.isEmpty()) {
            throw NoExistAccountException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.ACCOUNT_NOT_EXIST)
                            .message("The account: " + accountNo +
                                    " is not exist.")
                            .build()).build()).build();
        }
        return byAccountNo.get();
    }

    private void checkAccountIsActive(Account account) {
        if (!account.getIsActive()) {
            throw NoActiveAccountException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.NO_ACTIVE_ACCOUNT)
                            .message("The account: " + account.getAccountNo() +
                                    " is not active.")
                            .build()).build()).build();
        }
    }

    private void checkBalance(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw InsufficientBalanceException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.INSUFFICIENT_BALANCE)
                            .message("Balance: " + balance +
                                    " is less than " + amount)
                            .build()).build()).build();
        }
    }

    private void checkTokenUser(Account account) {
        String tokenUserPin = currentUser.getCurrentUser().getUsername();
        String accountUserPin = account.getUser().getPin();

        if (!tokenUserPin.equals(accountUserPin)) {
            throw InvalidUserAccessException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.INVALID_USER_ACCESS)
                            .message("User : " + tokenUserPin +
                                    " has not access for transfer with account : " + account.getAccountNo())
                            .build()).build()).build();
        }
    }

    private CommonResponseDTO<?> transferMoney(Account debitAccount, Account creditAccount, BigDecimal amount) {
        debitAccount.setBalance(debitAccount.getBalance().subtract(amount));
        creditAccount.setBalance(creditAccount.getBalance().add(amount));

        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("Transfer completed successfully.")
                .build()).data(AccountResponseDTO.builder()
                .accountNo(debitAccount.getAccountNo())
                .isActive(debitAccount.getIsActive())
                .balance(debitAccount.getBalance())
                .currency(debitAccount.getCurrency())
                .build()).build();
    }
}

