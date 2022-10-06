package com.livevox.challenge.app.call;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calls")
public class CallController {

    private final CallService service;

    @Autowired
    public CallController(final CallService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Call> list(final Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping(value = "routed")
    public Long countRoutedCalls() {
        return service.countRoutedCalls();
    }

}
