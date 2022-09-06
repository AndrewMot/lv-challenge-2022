package com.livevox.challenge.app.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/agents")
public class AgentApi {

    @Autowired
    private AgentDb db;

    @GetMapping
    public List<Agent> list() {
        return db.findAll();
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
