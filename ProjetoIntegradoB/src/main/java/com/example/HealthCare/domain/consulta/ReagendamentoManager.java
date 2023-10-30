package com.example.HealthCare.domain.consulta;

import com.example.HealthCare.domain.paciente.Paciente;
import com.example.HealthCare.domain.paciente.PacienteRepository;
import com.example.HealthCare.domain.profissional.Profissional;
import com.example.HealthCare.domain.profissional.ProfissionalRepository;
import com.example.HealthCare.domain.usuario.Usuario;
import com.example.HealthCare.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@EnableAsync
public class ReagendamentoManager {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private TokenService tokenService;

    @Async
    public void chamaListaEspera(String idConsulta) throws InterruptedException {
        System.out.println("Thread Criada" + Thread.currentThread());
        var consulta = consultaRepository.findById(idConsulta).get();
        var profissional = consulta.getProfissional();
        System.out.println(consulta.getListaEspera());
        while (!consulta.getListaEspera().isEmpty()){
            System.out.println("Entro no while");
            var paciente = consulta.chamaPaciente();
            System.out.println(paciente);
            consulta =chamaPaciente(paciente, consulta);
            if(consulta.getStatus() == Status.CONFIRMADO){
                System.out.println("Reagendou");
                break;
            }
            consulta = consultaRepository.save(consulta);
        }
        if(consulta.getStatus() == Status.REAGENDANDO){
            profissional.removeConsulta(consulta);
            profissionalRepository.save(profissional);
            consultaRepository.delete(consulta);
            System.out.println("Deleto Consulta");
        }
    }

    public Consulta chamaPaciente(Paciente paciente,Consulta c) throws InterruptedException {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusSeconds(30);
        var token = tokenService.gerarToken(paciente.getEmail(),end.toInstant(ZoneOffset.of("-03:00")));
        //Envia mensagem whatsapp
        var consulta = c;
        while(start.isBefore(end)){
            if(consulta.getPaciente().getId().equals(paciente.getId())){
                return consulta;
            }
            Thread.sleep(10000);
            start = LocalDateTime.now();
        }
        return null;
    }
}
