package com.safarov.tech_app.service;

import com.safarov.tech_app.dto.response.CommonResponseDTO;
import com.safarov.tech_app.dto.response.Status;
import com.safarov.tech_app.dto.response.StatusCode;
import com.safarov.tech_app.dto.response.mbdto.ValCursResponseDTO;
import com.safarov.tech_app.restclient.CbarRestClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrencyService {
    CbarRestClient cbarRestClient;

    public CommonResponseDTO<?> getCurrencies() {
        ValCursResponseDTO currency = cbarRestClient.getCurrency();

        return CommonResponseDTO.builder().status(Status.builder()
                .statusCode(StatusCode.SUCCESS)
                .message("All currencies have been successfully retrieved.")
                .build()).data(currency).build();
    }
}
