package com.fairrummy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HealthController {

    @RequestMapping(value = "/healthcheck/deep", method = RequestMethod.GET)
    @ResponseBody
    @GetMapping
    public ResponseEntity<Boolean> deepHealthCheck() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @RequestMapping(value = "/healthcheck/shallow", method = RequestMethod.GET)
    @ResponseBody
    @GetMapping
    public ResponseEntity<Boolean> shallowHealthCheck() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}