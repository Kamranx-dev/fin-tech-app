package com.safarov.tech_app.exception;

import com.safarov.tech_app.dto.response.CommonResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotActiveAccountException extends RuntimeException {
    CommonResponseDTO<?> commonResponseDTO;
}
