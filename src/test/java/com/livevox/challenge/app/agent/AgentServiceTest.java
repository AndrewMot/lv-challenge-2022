package com.livevox.challenge.app.agent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.livevox.challenge.app.DataGenerator;
import com.livevox.challenge.app.callcenter.CallCenter;
import com.livevox.challenge.app.callcenter.CallCenterRepository;
import com.livevox.challenge.app.response.exceptions.BadRequestException;
import com.livevox.challenge.app.response.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AgentServiceTest {

    @Mock
    private AgentRepository agentRepository;
    @Mock
    private CallCenterRepository callCenterRepository;

    @InjectMocks
    private AgentService agentService;

    @Test
    @DisplayName("When Agent doesn't exist then NotFoundException")
    void agentNotFound() {
        given(agentRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> agentService.assign(DataGenerator.AGENT_ID, DataGenerator.CALL_CENTER_ID));
    }

    @Test
    @DisplayName("When Call Center doesn't exist then NotFoundException")
    void callCenterNotFound() {
        final Agent agent = mock(Agent.class);
        given(agentRepository.findById(any())).willReturn(Optional.of(agent));
        given(callCenterRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> agentService.assign(DataGenerator.AGENT_ID, DataGenerator.CALL_CENTER_ID));
    }

    @Test
    @DisplayName("When Agent and Call Center exist then Agent active and Response")
    void agentActivated() {
        final Agent agent = mock(Agent.class);
        final CallCenter callCenter = mock(CallCenter.class);
        given(agentRepository.findById(any())).willReturn(Optional.of(agent));
        given(callCenterRepository.findById(any())).willReturn(Optional.of(callCenter));
        given(agentRepository.save(any())).willReturn(agent);
        when(agent.getFirstName()).thenReturn(DataGenerator.FIRST_NAME);
        when(agent.getLastName()).thenReturn(DataGenerator.LAST_NAME);
        when(agent.getId()).thenReturn(DataGenerator.AGENT_ID);
        when(callCenter.getPhone()).thenReturn(DataGenerator.PHONE_NUMBER);
        final AssignResponse response = agentService.assign(DataGenerator.AGENT_ID, DataGenerator.CALL_CENTER_ID);
        assertEquals(DataGenerator.FIRST_NAME, response.getFirstName());
        assertEquals(DataGenerator.LAST_NAME, response.getLastName());
        assertEquals(DataGenerator.PHONE_NUMBER, response.getPhone());
        assertEquals(DataGenerator.AGENT_ID, response.getId());
        assertEquals(DataGenerator.CALL_CENTER_ID, response.getCallCenterId());
        verify(agentRepository).save(agent);
    }

    @Test
    @DisplayName("When Agent doesn't exist then NotFoundException when unassigned")
    void agentNotFoundUnassign() {
        given(agentRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> agentService.unassign(DataGenerator.AGENT_ID));
    }

    @Test
    @DisplayName("When Agent exist then Agent inactive and Response")
    void agentUnactivated() {
        final Agent agent = mock(Agent.class);
        given(agentRepository.findById(any())).willReturn(Optional.of(agent));
        given(agentRepository.save(any())).willReturn(agent);
        when(agent.getFirstName()).thenReturn(DataGenerator.FIRST_NAME);
        when(agent.getLastName()).thenReturn(DataGenerator.LAST_NAME);
        when(agent.getId()).thenReturn(DataGenerator.AGENT_ID);
        final AssignResponse response = agentService.unassign(DataGenerator.AGENT_ID);
        assertEquals(DataGenerator.FIRST_NAME, response.getFirstName());
        assertEquals(DataGenerator.LAST_NAME, response.getLastName());
        assertNull(response.getPhone());
        assertEquals(DataGenerator.AGENT_ID, response.getId());
        assertNull(response.getCallCenterId());
        verify(agentRepository).save(agent);
    }

}
