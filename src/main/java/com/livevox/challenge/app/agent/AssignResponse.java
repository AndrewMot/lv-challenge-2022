package com.livevox.challenge.app.agent;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AssignResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private Long callCenterId;
}
