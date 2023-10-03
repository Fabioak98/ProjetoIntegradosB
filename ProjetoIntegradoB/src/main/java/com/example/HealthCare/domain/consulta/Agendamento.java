package com.example.HealthCare.domain.consulta;

import com.example.HealthCare.domain.paciente.Paciente;
import com.example.HealthCare.domain.paciente.PacienteRepository;
import com.example.HealthCare.domain.profissional.Profissional;
import com.example.HealthCare.domain.profissional.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

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

            return consulta;
        }
        else {
            return null;
        }
    }


    @DeleteMapping("/{id}")
    @Transactional
    public void cancelaConsulta(@PathVariable String id) {
    }
}
