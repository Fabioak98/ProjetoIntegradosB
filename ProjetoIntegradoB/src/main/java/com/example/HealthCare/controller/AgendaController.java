package com.example.HealthCare.controller;

import com.example.HealthCare.domain.consulta.Agendamento;
import com.example.HealthCare.domain.consulta.Consulta;
import com.example.HealthCare.domain.consulta.DadosAgendamentoConsulta;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private Agendamento agendamento;

    @PostMapping
    @Transactional
    public ResponseEntity agendaConsulta(@RequestBody @Valid DadosAgendamentoConsulta dados, UriComponentsBuilder uriComponentsBuilder){
        var consulta = agendamento.agendaConsulta(dados);
        if(consulta == null){
            throw new IllegalArgumentException("Horario nao disponivel");
        }
        var uri = uriComponentsBuilder.path("/consulta/{id}").buildAndExpand(consulta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity cancelaConsulta(@PathVariable String id){
        agendamento.cancelaConsulta(id);
        return ResponseEntity.ok().build();
    }

}
