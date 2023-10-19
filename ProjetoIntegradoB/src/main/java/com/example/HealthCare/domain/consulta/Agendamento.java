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

@Service
public class Agendamento {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

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

            adicionaListaEspera(profissional,dados.diasLivres(),paciente);

            return consulta;
        }
        else {
            return null;
        }
    }

    private void adicionaListaEspera(Profissional profissional, List<LocalDate> data, Paciente paciente) {
        if(data != null) {
            for (LocalDate l : data) {
                var dia = LocalDate.of(l.getYear(), l.getMonth(), l.getDayOfMonth());
                var dataM = dia.plusDays(1);

                Instant apartir = dia.atStartOfDay(ZoneId.systemDefault()).toInstant();
                Instant ate = dataM.atStartOfDay(ZoneId.systemDefault()).toInstant();


                List<Consulta> consultas = consultaRepository.findByDataBetweenAndProfissional(apartir,ate,profissional);
                System.out.println(consultas);
                consultas.forEach(consulta -> {
                    consulta.addListadeEspera(paciente);
                    consultaRepository.save(consulta);
                });
            }
        }
    }

    public void cancelaConsulta(String idConsulta, String idPaciente,String idProfissional){
        Consulta consulta = consultaRepository.findById(idConsulta).get();
        Profissional profissional = profissionalRepository.findById(idProfissional).get();
        Paciente paciente = pacienteRepository.findById(idPaciente).get();

        if(consulta.getPaciente() != paciente){
            throw new IllegalArgumentException("Esta consulta nao pertence a esse paciente");
        }
        if (consulta.getProfissional() != profissional){
            throw new IllegalArgumentException("Esta consulta nao pertence a esse profissional");
        }

        if(consulta.getListaEspera() != null){
            chamaListaEspera(consulta);
        }

    }

    private void chamaListaEspera(Consulta consulta) {
        var list = consulta.getListaEspera();
        for (Paciente pacient : list){
            //manda url

        }
    }


    @DeleteMapping("/{id}")
    @Transactional
    public void cancelaConsulta(@PathVariable String id) {
         var consulta = consultaRepository.findById(id).get();

    }
}
