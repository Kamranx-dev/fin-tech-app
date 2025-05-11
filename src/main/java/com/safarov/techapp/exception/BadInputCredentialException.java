package com.safarov.techapp.exception;

import com.safarov.techapp.dto.response.CommonResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BadInputCredentialException extends RuntimeException {
    CommonResponseDTO<?> responseDTO;
}
