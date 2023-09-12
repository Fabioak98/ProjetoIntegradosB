package com.example.HealthCare.controller;

import com.example.HealthCare.domain.profissional.DadosCadastroProfissional;
import com.example.HealthCare.domain.profissional.DadosDetalhamentoProfissional;
import com.example.HealthCare.domain.profissional.Profissional;
import com.example.HealthCare.domain.profissional.ProfissionalRepository;
import com.example.HealthCare.domain.usuario.DadosLogin;
import com.example.HealthCare.domain.usuario.Usuario;
import com.example.HealthCare.domain.usuario.UsuarioRepository;
import com.example.HealthCare.infra.security.DadosToken;
import com.example.HealthCare.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class AutenticacaoController {

    @Autowired
    private ProfissionalRepository medicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosLogin dados){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(),dados.senha());
        var authetication = manager.authenticate(authenticationToken);
        System.out.println(authetication);
        System.out.println(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authetication.getPrincipal());

        return ResponseEntity.ok(new DadosToken(tokenJWT));
    }

    @PostMapping("/cadastro/profissional")
    @Transactional
    public ResponseEntity cadastroProfissional(@RequestBody @Valid DadosCadastroProfissional dados, UriComponentsBuilder uriComponentsBuilder){
        var medico = new Profissional(dados);
        medicoRepository.save(medico);

        var usuario = new Usuario(dados.email(),passwordEncoder.encode(dados.senha()));
        usuarioRepository.save(usuario);


        var uri = uriComponentsBuilder.path("/profissional/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoProfissional(medico));
    }
}
