package com.livevox.challenge.app.vendor;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class VendorClient {

    private static final String BASE_URL = "https://external-api-challenge-2022.herokuapp.com/api/";
    private static final String CALLS_URL = String.format("%s%s", BASE_URL, "calls/daily");
    private static final String COUNTRIES_URL = String.format("%s%s", BASE_URL, "countries/{prefix}");
    private final Map<Integer, Set<CountryResponse.Country>> countries;
    private final RestTemplate restTemplate;

    public VendorClient(final RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
        this.countries = new HashMap<>();
    }

    public RemoteCallResponse fetchCalls() {
        final ResponseEntity<RemoteCallResponse> response = getResponse(CALLS_URL, RemoteCallResponse.class);
        final RemoteCallResponse defValue = RemoteCallResponse.builder().calls(Collections.emptyList()).build();
        return handleResponse(response, defValue);
    }

    public Set<CountryResponse.Country> fetchCountriesByPrefix(final int prefix) {
        if (countries.containsKey(prefix)) {
            log.info("Serving countries from cache");
            return countries.get(prefix);
        }
        final ResponseEntity<CountryResponse> response = getResponse(COUNTRIES_URL, CountryResponse.class, prefix);

        final CountryResponse countryResponse =
            handleResponse(response, CountryResponse.builder().countries(Collections.emptyList()).build());
        if (countryResponse.getCountries().isEmpty()) {
            return Collections.emptySet();
        }
        final HashSet<CountryResponse.Country> set = new HashSet<>(countryResponse.getCountries());
        countries.put(prefix, set);
        return set;
    }

    private <T> T handleResponse(final ResponseEntity<T> response, T defValue) {
        if (response == null) {
            return defValue;
        }
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("No OK response from Vendor. Status Code {}", response.getStatusCode());
            return defValue;
        }
        if (!response.hasBody()) {
            log.error("No body received from Vendor");
            return defValue;
        }
        return response.getBody();
    }

    private <T> ResponseEntity<T> getResponse(final String url, final Class<T> clazz, final Object... uriParams) {
        try {
            return restTemplate.getForEntity(url, clazz, uriParams);
        } catch (final Exception e) {
            log.error("Something went wrong while communicating with third party vendor", e);
        }
        return null;
    }

}
