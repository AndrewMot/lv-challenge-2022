package com.livevox.challenge.app.agent;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    @Query("SELECT agent FROM Agent agent WHERE agent.callCenterId = :callCenterId AND agent.extension = :extension ")
    Optional<Agent> findAgentByCallCenterIdAndExtension(@Param("callCenterId") final long callCenterId,
                                                        @Param("extension") final String extension);
}
