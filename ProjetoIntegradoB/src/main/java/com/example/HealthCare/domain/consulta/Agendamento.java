package com.example.HealthCare.domain.consulta;

import com.example.HealthCare.domain.paciente.Paciente;
import com.example.HealthCare.domain.paciente.PacienteRepository;
import com.example.HealthCare.domain.profissional.Profissional;
import com.example.HealthCare.domain.profissional.ProfissionalRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@EnableAsync
public class Agendamento {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ReagendamentoManager reagendamentoManager;

    public Consulta agendaConsulta(DadosAgendamentoConsulta dados){
        var consulta = new Consulta(dados);
        var paciente = pacienteRepository.findById(dados.idPaciente()).get();
        var profissional = profissionalRepository.findById(dados.idMedico()).get();
        if (profissional == null){
            throw new IllegalArgumentException("Profissional nao encontrado");
        }
        if (paciente == null){
            throw new IllegalArgumentException("Paciente nao encontrado");
        }
        consulta.setProfissional(profissional);
        consulta.setPaciente(paciente);

        if(profissional.validaHorario(consulta.getData())){
            consulta = consultaRepository.save(consulta);
            paciente.addConsulta(consulta);
            pacienteRepository.save(paciente);

            profissional.addConsulta(consulta);
            profissionalRepository.save(profissional);

            adicionaListaEspera(profissional,dados.diasLivres(),paciente,consulta.getData());

            return consulta;
        }
        else {
            return null;
        }
    }

    private void adicionaListaEspera(Profissional profissional, List<DayOfWeek> data, Paciente paciente, LocalDateTime consulta) {
        if(data != null) {
            var dia = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth());
            var dataM = LocalDate.of(consulta.getYear(), consulta.getMonth(), consulta.getDayOfMonth());

            Instant apartir = dia.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant ate = dataM.atStartOfDay(ZoneId.systemDefault()).toInstant();


            List<Consulta> consultas = consultaRepository.findByDataBetweenAndProfissional(apartir,ate,profissional);
            for (Consulta consulta1 : consultas) {
                DayOfWeek diaSem = consulta1.getData().getDayOfWeek();
                if (data.contains(diaSem)) {
                    var aux = consulta1.getListaEspera().stream().filter(p -> p.getId().equals( paciente.getId())).findFirst().orElse(null);
                    if(aux == null){
                        consulta1.addListadeEspera(paciente);
                        consultaRepository.save(consulta1);
                    }
                }
            }
        }
    }

    public void cancelaConsulta(String idConsulta, String idPaciente,String idProfissional) throws ExecutionException, InterruptedException {
        Consulta consulta = consultaRepository.findById(idConsulta).get();
        Profissional profissional = profissionalRepository.findById(idProfissional).get();
        Paciente paciente = pacienteRepository.findById(idPaciente).get();

        System.out.println(consulta.getPaciente().getId());
        System.out.println(paciente.getId());

        if(!consulta.getPaciente().getId().equals( paciente.getId())){
            throw new IllegalArgumentException("Esta consulta nao pertence a esse paciente");
        }
        if (!consulta.getProfissional().getId().equals(profissional.getId())){
            throw new IllegalArgumentException("Esta consulta nao pertence a esse profissional");
        }
        paciente.removeConsulta(consulta);
        pacienteRepository.save(paciente);

        consulta.setDescricao("");
        consulta.setStatus(Status.REAGENDANDO);

        consulta = consultaRepository.save(consulta);
        System.out.println(consulta.getStatus());

        System.out.println("Main Thread " + Thread.currentThread());
        reagendamentoManager.chamaListaEspera(consulta.getId());


    }

    public void reagendamento(DadosReagendamentoConsulta dados) throws ExecutionException, InterruptedException {
        var consultaAt = consultaRepository.findById(dados.idConsultaAT()).get();
        var paciente = pacienteRepository.findById(dados.idPaciente()).get();
        if(consultaAt.getPaciente() == null || consultaAt.getListaEspera().contains(paciente)){
            var consultaRg = consultaRepository.findById(dados.idConsultaRG()).get();
            if(consultaRg.getPaciente().getId().equals(dados.idPaciente())){
                consultaAt.setDescricao(consultaRg.getDescricao());
                consultaAt.setPaciente(paciente);
                consultaAt.setStatus(Status.CONFIRMADO);
                consultaRepository.save(consultaAt);

                cancelaConsulta(consultaRg.getId(), dados.idPaciente(), consultaRg.getProfissional().getId());
            }
        }
    }
}
