package com.safarov.tech_app.service;

import com.safarov.tech_app.config.security.JWTUtil;
import com.safarov.tech_app.dto.request.AuthenticationRequestDTO;
import com.safarov.tech_app.dto.response.AuthenticationResponseDTO;
import com.safarov.tech_app.dto.response.CommonResponseDTO;
import com.safarov.tech_app.dto.response.Status;
import com.safarov.tech_app.dto.response.StatusCode;
import com.safarov.tech_app.exception.BadInputCredentialsException;
import com.safarov.tech_app.util.DTOCheckUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserLoginService {
    DTOCheckUtil dtoCheckUtil;
    AuthenticationManager authenticationManager;
    JWTUtil jwtUtil;
    UserDetailsService userDetailsService;

    public CommonResponseDTO<?> loginUser(AuthenticationRequestDTO authenticationRequestDTO) {
        dtoCheckUtil.isValid(authenticationRequestDTO);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequestDTO.getPin(),
                    authenticationRequestDTO.getPassword()));
        } catch (Exception e) {
            throw BadInputCredentialsException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.BAD_CREDENTIALS)
                            .message("The credentials are incorrect. Pin: "
                                    + authenticationRequestDTO.getPin() + " or password: "
                                    + authenticationRequestDTO.getPassword() + " is wrong.")
                            .build()).build()).build();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequestDTO.getPin());
        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("Welcome to the Fintech application. Token was generated Successfully")
                .build()).data(AuthenticationResponseDTO.builder()
                .token(jwtUtil.createToken(userDetails)).build()).build();
    }
}
