package com.safarov.techapp.exception;

import com.safarov.techapp.dto.response.CommonResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvalidAmountException extends RuntimeException {
    CommonResponseDTO<?> commonResponseDTO;
}
