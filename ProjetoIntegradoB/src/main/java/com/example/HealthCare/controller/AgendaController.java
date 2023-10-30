package com.example.HealthCare.controller;

import com.example.HealthCare.domain.consulta.*;
import com.example.HealthCare.domain.profissional.DadosDetalhamentoProfissional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private Agendamento agendamento;

    @Autowired
    private ConsultaRepository repository;
    @GetMapping("/{id}")
    public ResponseEntity detalhamento(@PathVariable String id){
        var consulta = repository.findById(id).get();
        return ResponseEntity.ok(new DadosDetalhamentoConsulta(consulta));
    }

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

    @DeleteMapping
    @Transactional
    public ResponseEntity cancelaConsulta(@Valid @RequestBody DadosCancelamentoConsulta dados) throws ExecutionException, InterruptedException {
        agendamento.cancelaConsulta(dados.idConsulta(), dados.idPaciente(), dados.idProf());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reagendamento")
    @Transactional
    public ResponseEntity reagendamento(@RequestBody @Valid DadosReagendamentoConsulta dados) throws ExecutionException, InterruptedException {
        agendamento.reagendamento(dados);
        return ResponseEntity.ok().build();
    }
}
