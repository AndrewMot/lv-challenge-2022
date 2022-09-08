package com.livevox.challenge.app.vendor;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CountryResponse {

    private List<Country> countries;

    @NoArgsConstructor
    @Setter
    @Getter
    public static class Country {

        private String isoCode;
        private Integer phoneCode;
        private String name;
    }
}
