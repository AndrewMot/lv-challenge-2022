package com.livevox.challenge.app;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentDb extends JpaRepository<Agent, Long> {

}
