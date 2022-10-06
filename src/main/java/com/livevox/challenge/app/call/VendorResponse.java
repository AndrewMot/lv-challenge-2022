package com.livevox.challenge.app.call;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
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
public class VendorResponse {

    private List<VendorCall> calls;

    @NoArgsConstructor
    @Setter
    @Getter
    public static class VendorCall {

        private Long id;
        @JsonAlias("first_name")
        private String firstName;
        @JsonAlias("last_name")
        private String lastName;
        private String phone;
        @JsonAlias("agent_extension")
        private String agentExtension;
        @JsonAlias("call_date")
        private String callDate;
        @JsonAlias("call_time")
        private String callTime;
    }
}
