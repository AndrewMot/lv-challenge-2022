package com.livevox.challenge.app.callcenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/call-centers")
public class CallCenterController {

    private final CallCenterService callCenterService;

    @Autowired
    public CallCenterController(final CallCenterService callCenterService) {
        this.callCenterService = callCenterService;
    }

    @PostMapping
    public CallCenter create(@RequestBody final CallCenter callCenter) {
        return callCenterService.create(callCenter);
    }

    @GetMapping(value = "/{id}")
    public CallCenter retrieve(@PathVariable final Long id) {
        return callCenterService.retrieve(id);
    }
}
