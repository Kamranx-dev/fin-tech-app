package com.safarov.techapp.dto.response;

import com.safarov.techapp.entity.Account;
import com.safarov.techapp.entity.Currency;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    BigDecimal balance;
    Currency currency;
    Boolean isActive;
    Integer accountNo;

    public static AccountResponseDTO entityToDTO(Account account) {
        return AccountResponseDTO.builder()
                .balance(account.getBalance())
                .accountNo(account.getAccountNo())
                .isActive(account.getIsActive())
                .currency(account.getCurrency()).
                build();
    }
}
