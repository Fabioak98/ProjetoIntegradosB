package com.example.HealthCare.domain.consulta;

import com.example.HealthCare.domain.paciente.PacienteRepository;
import com.example.HealthCare.domain.profissional.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Agendamento {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    public void agendaConsulta(DadosAgendamentoConsulta dados){
        var medico = profissionalRepository.findById(dados.idMedico()).get();
        if(medico == null){
            throw new RuntimeException("Nenhum médico com esse id");
        }

        var paciente = pacienteRepository.findById(dados.idPaciente()).get();
        if(paciente == null){
            throw new RuntimeException("Nenhum paciente com esse id");
        }


    }

}
