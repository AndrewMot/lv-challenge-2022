package com.livevox.challenge.app.agent;

import com.livevox.challenge.app.callcenter.CallCenter;
import com.livevox.challenge.app.callcenter.CallCenterRepository;
import com.livevox.challenge.app.response.Constants;
import com.livevox.challenge.app.response.exceptions.BadRequestException;
import com.livevox.challenge.app.response.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    private final AgentRepository agentRepository;
    private final CallCenterRepository callCenterRepository;

    @Autowired
    public AgentService(final AgentRepository agentRepository, final CallCenterRepository callCenterRepository) {
        this.agentRepository = agentRepository;
        this.callCenterRepository = callCenterRepository;
    }

    public AssignResponse assign(final Long id, final Long callCenterId) {
        final Agent agent =
            agentRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.AGENT_NOT_FOUND_MESSAGE));
        final CallCenter callCenter = callCenterRepository.findById(callCenterId)
            .orElseThrow(() -> new BadRequestException(Constants.CALL_CENTER_NOT_FOUND_MESSAGE));
        agent.setCallCenterId(callCenterId);
        agent.setActive(true);
        agentRepository.save(agent);
        return AssignResponse.builder()
            .callCenterId(callCenterId)
            .id(agent.getId())
            .firstName(agent.getFirstName())
            .lastName(agent.getLastName())
            .phone(callCenter.getPhone())
            .build();
    }

    public AssignResponse unassign(final Long id) {
        final Agent agent =
            agentRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.AGENT_NOT_FOUND_MESSAGE));
        agent.setCallCenterId(null);
        agent.setActive(false);
        agentRepository.save(agent);
        return AssignResponse.builder()
            .callCenterId(null)
            .id(agent.getId())
            .firstName(agent.getFirstName())
            .lastName(agent.getLastName())
            .phone(null)
            .build();
    }
}
