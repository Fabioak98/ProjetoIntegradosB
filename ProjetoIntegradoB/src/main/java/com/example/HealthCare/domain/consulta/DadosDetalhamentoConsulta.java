package com.example.HealthCare.domain.consulta;

import com.example.HealthCare.domain.paciente.Paciente;
import com.example.HealthCare.domain.profissional.Profissional;

import java.time.LocalDateTime;

public record DadosDetalhamentoConsulta(String idConsulta, String idPaciente, String idProfissional, String descricao, String lista) {
    public DadosDetalhamentoConsulta(Consulta c){
        this(c.getId(), c.getPaciente().getId(), c.getProfissional().getId(), c.getDescricao(), c.getListaEspera().toString());
    }
}
