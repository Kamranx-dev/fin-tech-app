package com.safarov.tech_app.restclient;

import com.safarov.tech_app.config.ApplicationConfig;
import com.safarov.tech_app.dto.response.CommonResponseDTO;
import com.safarov.tech_app.dto.response.Status;
import com.safarov.tech_app.dto.response.StatusCode;
import com.safarov.tech_app.dto.response.mbdto.ValCursResponseDTO;
import com.safarov.tech_app.exception.CbarRestException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CbarRestClient {
    RestTemplate restTemplate;

    public ValCursResponseDTO getCurrency() {
        ValCursResponseDTO responseDTO = null;
        try {
            responseDTO = restTemplate.getForObject(ApplicationConfig.urlMB, ValCursResponseDTO.class);
            if (Objects.isNull(responseDTO) || Objects.isNull(responseDTO.getValTypeList()))
                throwCbarRestException();
        } catch (Exception e) {
            e.printStackTrace();
            throwCbarRestException();
        }
        return responseDTO;
    }

    private void throwCbarRestException() {
        throw CbarRestException.builder().commonResponseDTO(CommonResponseDTO.builder()
                .status(Status.builder()
                        .statusCode(StatusCode.CBAR_ERROR)
                        .message("Error happened while getting currency from Cbar.").build()).build()).build();
    }
}
