package com.example.HealthCare.controller;

import com.example.HealthCare.domain.consulta.DadosDetalhamentoConsulta;
import com.example.HealthCare.domain.file.FileServiceImpl;
import com.example.HealthCare.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @Autowired
    private FileServiceImpl fileService;


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

    @GetMapping("/consultas/{id}")
    public ResponseEntity consutasPaciente(@PathVariable String id){
        var paciente = repository.findById(id).get();
        var lista = paciente.getConsultas().stream().map(DadosDetalhamentoConsulta::new).toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/upload")
    @Transactional
    public ResponseEntity<String> uploadFoto(@RequestParam MultipartFile foto, @RequestParam String id, UriComponentsBuilder uriComponentsBuilder) throws IOException {
        var paciente = repository.findById(id).get();
        if(paciente !=null || foto != null){
            String filename = foto.getOriginalFilename();
            String tipo = filename.substring(filename.lastIndexOf(".") + 1);
            fileService.uploadFile(foto, paciente.getId());
            paciente.setFoto("https://storage.googleapis.com/pib-bucket-fots/" + id + "." + tipo);
            repository.save(paciente);
        }
        return ResponseEntity.ok().build();
    }


}
