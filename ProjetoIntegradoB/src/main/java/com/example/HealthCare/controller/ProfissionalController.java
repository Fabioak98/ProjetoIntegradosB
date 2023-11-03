package com.example.HealthCare.controller;

import com.example.HealthCare.domain.avaliacao.DadosAvaliacao;
import com.example.HealthCare.domain.consulta.ConsultaRepository;
import com.example.HealthCare.domain.consulta.DadosDetalhamentoConsulta;
import com.example.HealthCare.domain.file.FileServiceImpl;
import com.example.HealthCare.domain.profissional.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/profissional")
public class ProfissionalController {

    @Autowired
    private ProfissionalRepository repository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private FileServiceImpl fileService;

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
    public ResponseEntity registraHorarios(@RequestBody @Valid DadosCadastraAgenda dados, UriComponentsBuilder uriComponentsBuilder){
        var profissional = repository.findById(dados.id()).get();

        profissional.atualizaAgenda(dados.horarios());

        var uri = uriComponentsBuilder.path("/profissional/perfil/{id}").buildAndExpand(profissional.getId()).toUri();

        repository.save(profissional);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/horarios/{id}")
    public ResponseEntity pegaAgenda(@PathVariable String id){
        var profissional = repository.findById(id).get();
        return ResponseEntity.ok(new DadosAgendaProfissional(profissional.getAgenda()));
    }

    @GetMapping(value = {"/busca/{especialidade}&{cidade}"})
    public ResponseEntity<Page<DadosListagemProfissional>> consultaEspecialidade(@PathVariable(required = false) String especialidade,@PathVariable(required = false) String cidade, @PageableDefault Pageable page){
        Page<DadosListagemProfissional> lista;
        if(cidade.isEmpty() && !especialidade.isEmpty()){
            lista = repository.findByEspecialidadeAndAtivoTrue(especialidade.toUpperCase(),page).map(DadosListagemProfissional ::new);
        } else if (!cidade.isEmpty() && especialidade.isEmpty()) {
           lista  = repository.findByEndereco_CidadeAndAtivoTrue(cidade,page).map(DadosListagemProfissional ::new);
        }
        else if (!cidade.isEmpty() && !especialidade.isEmpty()) {
            lista = repository.findByEspecialidadeAndEndereco_CidadeAndAtivoTrue(especialidade.toUpperCase(), cidade,page).map(DadosListagemProfissional::new);
        }else {
            return  ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/avaliacao")
    @Transactional
    public ResponseEntity avaliaProfissional(@RequestBody DadosAvaliacao dados){
        var profissional = repository.findById(dados.idProfissional()).get();

        profissional.novaNota(dados.nota());
        repository.save(profissional);

        return ResponseEntity.ok(new DadosDetalhamentoProfissional(profissional));
    }

    @GetMapping("/consultas/{data}&{id}")
    public ResponseEntity<List<DadosDetalhamentoConsulta>> consultaDia(@PathVariable(value = "data") String data, @PathVariable(value = "id") String id){
        var profissional = repository.findById(id).get();
        System.out.println(profissional);
        if(profissional != null){
            var dia = LocalDate.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            var dataM = dia.plusDays(1);

            Instant apartir = dia.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant ate = dataM.atStartOfDay(ZoneId.systemDefault()).toInstant();


            var consultas = consultaRepository.findByDataBetweenAndProfissional(apartir,ate,profissional).stream().map(DadosDetalhamentoConsulta:: new).toList();
            return ResponseEntity.ok(consultas);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/upload")
    @Transactional
    public ResponseEntity<String> uploadFoto(@RequestParam MultipartFile foto, @RequestParam String idProfissional, UriComponentsBuilder uriComponentsBuilder) throws IOException {
        var profissional = repository.findById(idProfissional).get();
        if(profissional!=null || foto != null){
            String filename = foto.getOriginalFilename();
            String tipo = filename.substring(filename.lastIndexOf(".") + 1);
            fileService.uploadFile(foto,profissional.getId());
            profissional.setFoto("https://storage.googleapis.com/pib-bucket-fots/" + idProfissional + "." + tipo);
            repository.save(profissional);
        }
        return ResponseEntity.ok().build();
    }
}
