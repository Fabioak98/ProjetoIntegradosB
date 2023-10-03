package com.example.HealthCare.controller;

import com.example.HealthCare.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;


    @GetMapping
    public Page<DadosListagemPaciente> listar(@PageableDefault(size = 1,sort = {"nome"})Pageable paginacao){
        return repository.findAll(paginacao).map(DadosListagemPaciente::new);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable String id){
        var paciente = repository.findById(id).get();
        paciente.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhamento(@PathVariable String id){
        var paciente = repository.findById(id).get();
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }


}
