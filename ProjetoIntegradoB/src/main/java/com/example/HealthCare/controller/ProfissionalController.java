package com.example.HealthCare.controller;

import com.example.HealthCare.domain.profissional.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/profissional")
public class ProfissionalController {

    @Autowired
    private ProfissionalRepository repository;


    @GetMapping
    public ResponseEntity<Page<DadosListagemProfissional>> listar(@PageableDefault(size = 1,sort = {"nome"}) Pageable paginacao){
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemProfissional::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoProfissional dados){
        var profissional = repository.findById(dados.id()).get();
        profissional.atualizarInformacoes(dados);

        repository.save(profissional);
        return ResponseEntity.ok(new DadosDetalhamentoProfissional(profissional));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable String id){
        var profissional = repository.findById(id).get();
        profissional.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhamento(@PathVariable String id){
        var profissional = repository.findById(id).get();
        return ResponseEntity.ok(new DadosDetalhamentoProfissional(profissional));
    }

    @PostMapping("/horarios")
    @Transactional
    public ResponseEntity registraHorarios(@RequestBody @Valid DadosCrudPerfil dados,UriComponentsBuilder uriComponentsBuilder){
        var profissional = repository.findById(dados.id()).get();

        profissional.atualizaAgenda(dados);

        var uri = uriComponentsBuilder.path("/profissional/perfil/{id}").buildAndExpand(profissional.getId()).toUri();

        repository.save(profissional);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/horarios/{id}")
    public ResponseEntity pegaAgenda(@PathVariable String id){
        var profissional = repository.findById(id).get();
        return ResponseEntity.ok(new DadosAgendaProfissional(profissional.getAgenda()));
    }

    @GetMapping("/busca_especialidade/{especialidade}")
    public ResponseEntity<List<DadosListagemProfissional>> consultaEspecialidade(@PathVariable String especialidade){
        var lista = repository.findByEspecialidade(especialidade.toUpperCase()).stream().map(DadosListagemProfissional:: new).toList();
        return ResponseEntity.ok(lista);
    }
}
