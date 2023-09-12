package com.example.HealthCare.controller;

import com.example.HealthCare.domain.consulta.Agendamento;
import com.example.HealthCare.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private Agendamento agendamento;

    @PostMapping
    @Transactional
    public ResponseEntity agendaConsulta(@RequestBody DadosAgendamentoConsulta dados){


        return ResponseEntity.ok().build();
    }

}
