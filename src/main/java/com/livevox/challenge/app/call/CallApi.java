package com.livevox.challenge.app.call;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calls")
public class CallApi {

    @Autowired
    private CallDb db;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Call> list() {
        return db.findAll();
    }

}
