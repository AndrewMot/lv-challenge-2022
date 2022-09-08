package com.livevox.challenge.app.call;

import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class VendorClient {

    private static final String URL = "https://external-api-challenge-2022.herokuapp.com/api/calls/daily";
    private final RestTemplate restTemplate;

    public VendorClient(final RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public VendorResponse fetch() {
        final ResponseEntity<VendorResponse> response = getResponse();
        if (response == null) {
            return VendorResponse.builder().calls(Collections.emptyList()).build();
        }
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("No OK response from Vendor. Status Code {}", response.getStatusCode());
            return VendorResponse.builder().calls(Collections.emptyList()).build();
        }
        if (!response.hasBody()) {
            log.error("No body received from Vendor");
            return VendorResponse.builder().calls(Collections.emptyList()).build();
        }
        return response.getBody();
    }

    private ResponseEntity<VendorResponse> getResponse() {
        try {
            return restTemplate.getForEntity(URL, VendorResponse.class);
        } catch (final Exception e) {
            log.error("Something went wrong while communicating with third party vendor", e);
        }
        return null;
    }

}
