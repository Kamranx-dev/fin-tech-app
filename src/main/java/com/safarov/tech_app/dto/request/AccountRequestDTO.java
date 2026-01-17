package com.safarov.tech_app.dto.request;

import com.safarov.tech_app.entity.Currency;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    BigDecimal balance;
    Currency currency;
    Boolean isActive;
    Integer accountNo;
}
