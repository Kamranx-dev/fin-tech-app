package com.safarov.tech_app.controller;

import com.safarov.tech_app.dto.request.AccountToAccountRequestDTO;
import com.safarov.tech_app.service.AccountService;
import com.safarov.tech_app.service.TransferMoneyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    AccountService accountService;
    TransferMoneyService transferMoneyService;

    @GetMapping("/accounts")
    public ResponseEntity<?> getAccounts() {
        return new ResponseEntity<>(accountService.getAccountsForUser(), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferAmount(@RequestBody AccountToAccountRequestDTO requestDTO) {
        return new ResponseEntity<>(transferMoneyService.account2account(requestDTO), HttpStatus.OK);
    }
}
