package com.livevox.challenge.app.agent;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name = "agents")
@Data
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agent_id")
    @SequenceGenerator(name = "agent_id", sequenceName = "agent_id_seq", allocationSize = 1, initialValue = 100)
    private Long id;

    private String firstName;

    private String lastName;

    private String extension;

    private Boolean active;

    private Long callCenterId;
}
