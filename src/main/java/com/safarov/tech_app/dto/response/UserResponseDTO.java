package com.safarov.tech_app.dto.response;

import com.safarov.tech_app.dto.request.AccountRequestDTO;
import com.safarov.tech_app.entity.TechUser;
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

    Long id;
    String name;
    String surname;
    String pin;
    String password;
    String role;
    List<AccountResponseDTO> accountResponseDTOList;

    public static UserResponseDTO entityResponse(TechUser techUser) {
        List<AccountResponseDTO> accountList = new ArrayList<>();
        techUser.getAccountList().forEach(account -> accountList.add(AccountResponseDTO.builder()
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .isActive(account.getIsActive())
                .accountNo(account.getAccountNo()).build()));

        return UserResponseDTO.builder()
                .id(techUser.getId())
                .name(techUser.getName())
                .surname(techUser.getSurname())
                .pin(techUser.getPin())
                .password(techUser.getPassword())
                .role(techUser.getRole())
                .accountResponseDTOList(accountList).build();
    }
}
