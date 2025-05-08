package com.safarov.techapp.dto.response;

import com.safarov.techapp.dto.request.AccountRequestDTO;
import com.safarov.techapp.entity.TechUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    long id;
    String name;
    String surname;
    String password;
    String pin;
    String role;
    List<AccountRequestDTO> accountRequestDTOList;

    public static UserResponseDTO entityResponse(TechUser user) {
        List<AccountRequestDTO> accountList = new ArrayList<>();
        user.getAccountList().forEach(account -> accountList.add(AccountRequestDTO.builder()
                .balance(account.getBalance())
                .accountNo(account.getAccountNo())
                .currency(account.getCurrency())
                .isActive(account.getIsActive())
                .build()));

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .password(user.getPassword())
                .pin(user.getPin())
                .role(user.getRole())
                .accountRequestDTOList(accountList)
                .build();
    }
}
