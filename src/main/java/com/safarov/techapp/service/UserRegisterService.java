package com.safarov.techapp.service;

import com.safarov.techapp.dto.request.AccountRequestDTO;
import com.safarov.techapp.dto.request.UserRequestDTO;
import com.safarov.techapp.dto.response.CommonResponseDTO;
import com.safarov.techapp.dto.response.Status;
import com.safarov.techapp.dto.response.StatusCode;
import com.safarov.techapp.dto.response.UserResponseDTO;
import com.safarov.techapp.entity.Account;
import com.safarov.techapp.entity.TechUser;
import com.safarov.techapp.exception.AccountAlreadyExist;
import com.safarov.techapp.exception.UserAlreadyExistException;
import com.safarov.techapp.repositories.AccountRepository;
import com.safarov.techapp.repositories.UserRepository;
import com.safarov.techapp.util.DTOCheckUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRegisterService {
    DTOCheckUtil dtoCheckUtil;
    AccountRepository accountRepository;
    UserRepository userRepository;

    public CommonResponseDTO<?> saveUser(UserRequestDTO userRequestDTO) {
        dtoCheckUtil.isValid(userRequestDTO);
        checkUserExist(userRequestDTO);
        checkAccountExist(userRequestDTO);
        TechUser user = createUserEntity(userRequestDTO);
        TechUser savedUser = userRepository.save(user);

        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.Success)
                .message("Registration has been successfully")
                .build()).data(UserResponseDTO.entityResponse(savedUser)).build();
    }

    private void checkUserExist(UserRequestDTO userRequestDTO) {
        Optional<TechUser> optionalTechUser = userRepository.findUserByPin(userRequestDTO.getPin());
        if (optionalTechUser.isPresent())
            throw UserAlreadyExistException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.ALREADY_EXIST)
                            .message("User with pin:" + userRequestDTO.getPin() + " is exists." +
                                    " Please enter a pin that not bean register before.")
                            .build()).build()).build();
    }

    private TechUser createUserEntity(UserRequestDTO userRequestDTO) {
        TechUser user = TechUser.builder()
                .name(userRequestDTO.getName())
                .surname(userRequestDTO.getSurname())
                .pin(userRequestDTO.getPin())
                .password(userRequestDTO.getPassword())
                .role("USER_ROLE")
                .build();
        user.addAccountToUser(userRequestDTO.getAccountRequestDTOList());
        return user;
    }

    private void checkAccountExist(UserRequestDTO userRequestDTO) {
        List<Integer> accountNoList = accountRepository.findAll().stream()
                .map(Account::getAccountNo)
                .collect(Collectors.toList());

        userRequestDTO.getAccountRequestDTOList()
                .stream()
                .map(AccountRequestDTO::getAccountNo)
                .filter(accountNoList::contains)
                .findFirst()
                .ifPresent(duplicateAccountNo -> {
                            throw AccountAlreadyExist.builder()
                                    .commonResponseDTO(CommonResponseDTO.builder().status(Status.builder()
                                            .statusCode(StatusCode.ALREADY_EXIST)
                                            .message("Duplicate account no:" + duplicateAccountNo)
                                            .build()).build()).build();
                        }
                );
    }
}
