package com.example.HealthCare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/twilio")
public class TwilioController {

    @PostMapping
    public void reagendaConsulta(@RequestBody Map<String,String> dados){


    }
}
