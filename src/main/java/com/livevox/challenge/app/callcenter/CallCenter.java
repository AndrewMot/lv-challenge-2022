package com.livevox.challenge.app.callcenter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "call_centers")
@Data
public class CallCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "call_center_id")
    @SequenceGenerator(name = "call_center_id", sequenceName = "call_center_id_seq", allocationSize = 1, initialValue = 100)
    private Long id;

    private String name;

    private String countryName;

    private String phone;
}
