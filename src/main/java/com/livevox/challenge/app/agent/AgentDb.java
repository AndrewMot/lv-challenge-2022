package com.livevox.challenge.app.agent;

import com.livevox.challenge.app.agent.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentDb extends JpaRepository<Agent, Long> {

}
