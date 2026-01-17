package com.safarov.tech_app.config.security;

import com.safarov.tech_app.dto.response.CommonResponseDTO;
import com.safarov.tech_app.dto.response.Status;
import com.safarov.tech_app.dto.response.StatusCode;
import com.safarov.tech_app.entity.TechUser;
import com.safarov.tech_app.exception.NoSuchUserExistException;
import com.safarov.tech_app.repositories.TechUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsServiceImpl implements UserDetailsService {
    TechUserRepository userRepository;
    Logger logger;

    @Override
    public UserDetails loadUserByUsername(String pin) throws UsernameNotFoundException {
        Optional<TechUser> optionalTechUser = userRepository.findByPin(pin);
        if (optionalTechUser.isPresent()) {
            return new UserDetailsImpl(optionalTechUser.get());
        } else {
            logger.error("There is no pin {}", pin);
            throw NoSuchUserExistException.builder().commonResponseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.USER_NOT_EXIST)
                            .message("There is no pin: " + pin)
                            .build()).build()).build();
        }
    }
}
