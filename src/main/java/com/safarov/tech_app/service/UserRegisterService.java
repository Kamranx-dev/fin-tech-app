package com.safarov.tech_app.service;

import com.safarov.tech_app.dto.request.AccountRequestDTO;
import com.safarov.tech_app.dto.request.UserRequestDTO;
import com.safarov.tech_app.dto.response.CommonResponseDTO;
import com.safarov.tech_app.dto.response.Status;
import com.safarov.tech_app.dto.response.StatusCode;
import com.safarov.tech_app.dto.response.UserResponseDTO;
import com.safarov.tech_app.entity.Account;
import com.safarov.tech_app.entity.TechUser;
import com.safarov.tech_app.exception.AccountAlreadyExistException;
import com.safarov.tech_app.exception.UserAlreadyExistException;
import com.safarov.tech_app.repositories.AccountRepository;
import com.safarov.tech_app.repositories.TechUserRepository;
import com.safarov.tech_app.util.DTOCheckUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRegisterService {
    DTOCheckUtil dtoCheckUtil;
    TechUserRepository userRepository;
    AccountRepository accountRepository;

    public CommonResponseDTO<?> saveUser(UserRequestDTO userRequestDTO) {
        dtoCheckUtil.isValid(userRequestDTO);
        checkUserExist(userRequestDTO);
        checkAccountExist(userRequestDTO);
        TechUser techUser = createUserEntity(userRequestDTO);
        userRepository.save(techUser);

        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("Registration has been successful")
                .build()).data(UserResponseDTO.entityResponse(techUser)).build();
    }

    private void checkUserExist(UserRequestDTO userRequestDTO) {
        String pin = userRequestDTO.getPin();
        if (userRepository.existsByPin(pin))
            throw UserAlreadyExistException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.ALREADY_EXIST)
                            .message("User with pin:" + pin +
                                    " is exist. Please enter a pin that not been register before.")
                            .build()).build()).build();
    }

    private TechUser createUserEntity(UserRequestDTO userRequestDTO) {
        TechUser techUser = TechUser.builder()
                .name(userRequestDTO.getName())
                .surname(userRequestDTO.getSurname())
                .pin(userRequestDTO.getPin())
                .password(userRequestDTO.getPassword())
                .role("USER_ROLE").build();
        techUser.addAccountToUser(userRequestDTO.getAccountRequestDTOList());
        return techUser;
    }

    private void checkAccountExist(UserRequestDTO userRequestDTO) {
        List<Integer> accountNoList = userRequestDTO.getAccountRequestDTOList()
                .stream()
                .map(AccountRequestDTO::getAccountNo)
                .collect(Collectors.toList());
        List<Integer> existAccounNotList = accountRepository.getExistsAccountNo(accountNoList);
        if (!existAccounNotList.isEmpty())
            throw AccountAlreadyExistException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.ALREADY_EXIST)
                            .message("Duplicate account no: " + existAccounNotList)
                            .build()).build()).build();

    }
}
