package com.safarov.techapp.config.security;

import com.safarov.techapp.dto.response.CommonResponseDTO;
import com.safarov.techapp.dto.response.Status;
import com.safarov.techapp.dto.response.StatusCode;
import com.safarov.techapp.entity.TechUser;
import com.safarov.techapp.exception.NoSuchUserExistException;
import com.safarov.techapp.repositories.UserRepository;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    UserRepository userRepository;
    Logger logger;

    @Override
    public UserDetails loadUserByUsername(String pin) throws UsernameNotFoundException {
        Optional<TechUser> userRepositoryByPin = userRepository.findUserByPin(pin);
        if (userRepositoryByPin.isPresent()) {
            return new UserDetailsImpl(userRepositoryByPin.get());
        } else {
            logger.error("There is no user with pin {}", pin);
            throw NoSuchUserExistException.builder().responseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.USER_NOT_EXIST)
                            .message("There is no user with pin:" + pin)
                            .build()).build()).build();
        }
    }
}
