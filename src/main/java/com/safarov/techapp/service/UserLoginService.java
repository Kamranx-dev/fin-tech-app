package com.safarov.techapp.service;

import com.safarov.techapp.dto.request.AuthenticationRequestDTO;
import com.safarov.techapp.dto.response.CommonResponseDTO;
import com.safarov.techapp.dto.response.Status;
import com.safarov.techapp.dto.response.StatusCode;
import com.safarov.techapp.exception.BadInputCredentialException;
import com.safarov.techapp.util.DTOCheckUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserLoginService {
    AuthenticationManager authenticationManager;
    DTOCheckUtil dtoCheckUtil;

    public CommonResponseDTO<?> loginUser(AuthenticationRequestDTO authenticationRequestDTO) {
        try {
            dtoCheckUtil.isValid(authenticationRequestDTO);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequestDTO.getPin(),
                    authenticationRequestDTO.getPassword()
            ));
        } catch (Exception e) {
            throw BadInputCredentialException.builder().responseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.BAD_CREDENTIALS)
                            .message("The credentials are incorrect. Pin: " +
                                    authenticationRequestDTO.getPin() + " or password: " +
                                    authenticationRequestDTO.getPassword() + "is wrong.")
                            .build()).build()).build();
        }
        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("Welcome to the Fintech application")
                .build()).data(authenticationRequestDTO).build();
    }
}
