package com.safarov.tech_app.util;

import com.safarov.tech_app.dto.request.UserRequestDTO;
import com.safarov.tech_app.dto.response.CommonResponseDTO;
import com.safarov.tech_app.dto.response.Status;
import com.safarov.tech_app.dto.response.StatusCode;
import com.safarov.tech_app.exception.InvalidDTOException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DTOCheckUtil {
    Logger logger;

    public void isValid(UserRequestDTO userRequestDTO) {
        checkDtoInput(userRequestDTO.getName());
        checkDtoInput(userRequestDTO.getSurname());
        checkDtoInput(userRequestDTO.getPin());
        checkDtoInput(userRequestDTO.getPassword());
        checkDtoInput(userRequestDTO.getAccountRequestDTOList());
    }

    private <T> void checkDtoInput(T t) {
        if (Objects.isNull(t) || t.toString().isBlank()) {
            logger.error("DTO input is null or empty");
            throw InvalidDTOException.builder().responseDTO(CommonResponseDTO.builder()
                    .status(Status.builder()
                            .statusCode(StatusCode.INVALID_DTO)
                            .message("DTO input is null or empty")
                            .build()).build()).build();
        }

    }
}
