package com.safarov.techapp.dto.response;

import com.safarov.techapp.entity.Account;
import com.safarov.techapp.exception.NoActiveAccountException;
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
public class AccountResponseDTOList implements Serializable {
    private final static long serialVersionUID = 1L;

    List<AccountResponseDTO> accountResponseDTOList;

    public static AccountResponseDTOList entityToDto(List<Account> accountList) {
        List<AccountResponseDTO> accountResponseDTOList = new ArrayList<>();
        accountList.forEach(account -> accountResponseDTOList.
                add(AccountResponseDTO.entityToDTO(account)));

        return AccountResponseDTOList.builder()
                .accountResponseDTOList(accountResponseDTOList).build();
    }
}
