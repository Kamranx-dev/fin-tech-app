package com.safarov.tech_app.service;

import com.safarov.tech_app.dto.request.AccountToAccountRequestDTO;
import com.safarov.tech_app.dto.response.AccountResponseDTO;
import com.safarov.tech_app.dto.response.CommonResponseDTO;
import com.safarov.tech_app.dto.response.Status;
import com.safarov.tech_app.dto.response.StatusCode;
import com.safarov.tech_app.dto.response.mbdto.ValuteResponseDTO;
import com.safarov.tech_app.entity.Account;
import com.safarov.tech_app.entity.Currency;
import com.safarov.tech_app.exception.*;
import com.safarov.tech_app.repositories.AccountRepository;
import com.safarov.tech_app.restclient.CbarRestClient;
import com.safarov.tech_app.util.AccountCheckUtil;
import com.safarov.tech_app.util.CurrentUser;
import com.safarov.tech_app.util.DTOCheckUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferMoneyService {
    CurrentUser currentUser;
    CbarRestClient cbarRestClient;
    DTOCheckUtil dtoCheckUtil;
    AccountCheckUtil accountCheckUtil;
    AccountRepository accountRepository;


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
        transferMoney(debitAccount, creditAccount, amount);
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

    private void transferMoney(Account debitAccount, Account creditAccount, BigDecimal amount) {
        if (debitAccount.getCurrency().equals(creditAccount.getCurrency())) {
            debitAccount.setBalance(debitAccount.getBalance().subtract(amount));
            creditAccount.setBalance(creditAccount.getBalance().add(amount));
        } else {
            BigDecimal debitCurrencyRate = getCurrencyRate(debitAccount.getCurrency());
            BigDecimal creditCurrencyRate = getCurrencyRate(creditAccount.getCurrency());
            BigDecimal conversionRate = debitCurrencyRate.divide(creditCurrencyRate, 6, RoundingMode.HALF_UP);
            debitAccount.setBalance(debitAccount.getBalance().subtract(amount));
            creditAccount.setBalance(creditAccount.getBalance().add(amount.multiply(conversionRate)));
        }
    }

    private BigDecimal getCurrencyRate(Currency currency) {
        if (Currency.AZN.equals(currency))
            return BigDecimal.ONE;
        ValuteResponseDTO valute = getValute(currency);
        return valute.getValue().divide(valute.getNominal(), 10, RoundingMode.HALF_UP);
    }

    private List<ValuteResponseDTO> getValuteList() {
        return cbarRestClient.getCurrency()
                .getValTypeList().stream()
                .flatMap(valType -> Optional.ofNullable(valType
                        .getValuteList()).orElse(List.of()).stream())
                .collect(Collectors.toList());
    }

    private ValuteResponseDTO getValute(Currency currency) {
        return getValuteList().stream()
                .filter(valute -> valute.getCode().equals(currency.toString()))
                .findFirst()
                .orElseThrow(() -> CbarRestException.builder().commonResponseDTO(CommonResponseDTO.builder()
                        .status(Status.builder().statusCode(StatusCode.CBAR_ERROR)
                                .message("Currency rate not found for: " + currency)
                                .build()).build()).build());
    }

}
