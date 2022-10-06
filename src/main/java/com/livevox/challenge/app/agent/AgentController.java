package com.livevox.challenge.app.agent;

import java.util.List;
import java.util.Optional;

import com.livevox.challenge.app.response.Constants;
import com.livevox.challenge.app.response.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/agents")
public class AgentController {

    @Autowired
    private AgentRepository db;

    private final AgentService agentService;

    @Autowired
    public AgentController(final AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping
    public List<Agent> list() {
        return db.findAll();
    }

    @PatchMapping(value = "{id}/assign")
    public AssignResponse assign(@PathVariable Long id, @RequestBody AssignRequest call) {
        return Optional.ofNullable(call.getCallCenterId())
            .map(callCenterId -> agentService.assign(id, callCenterId))
            .orElseThrow(() -> new BadRequestException(Constants.CALL_CENTER_ID_REQUIRED_MESSAGE));
    }

    @PostMapping
    public Agent create(@RequestBody Agent agent) {
        return db.save(agent);
    }

    @GetMapping(value = "/{id}")
    public Agent getOne(@PathVariable(required = true) Long id) {
        return db.findById(id).orElseThrow();
    }

}
