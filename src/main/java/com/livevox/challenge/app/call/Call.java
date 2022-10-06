package com.livevox.challenge.app.call;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "calls")
@Data
public class Call {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "call_id")
    @SequenceGenerator(name = "call_id", sequenceName = "call_id_seq", allocationSize = 1, initialValue = 100)
    private Long id;

    private String customerFullName;

    private String customerPhone;

    private Integer customerCountryPhoneCode;

    private String agentExtension;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receivedOn;

}
