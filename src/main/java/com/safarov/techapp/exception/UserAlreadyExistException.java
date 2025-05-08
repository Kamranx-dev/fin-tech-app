package com.safarov.techapp.exception;

import com.safarov.techapp.dto.response.CommonResponseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAlreadyExistException extends RuntimeException {
    CommonResponseDTO<?> commonResponseDTO;
}
