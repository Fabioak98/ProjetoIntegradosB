package com.example.HealthCare.domain.consulta;

import com.example.HealthCare.domain.paciente.Paciente;
import com.example.HealthCare.domain.profissional.Profissional;

import java.time.LocalDateTime;

public record DadosDetalhamentoConsulta(String idConsulta, String Paciente, String idProfissional,
                                        String descricao, String lista, LocalDateTime horarios, Status status,
                                        String idPacienteChamado) {
    public DadosDetalhamentoConsulta(Consulta c){
        this(c.getId(), c.getPaciente().toString(), c.getProfissional().toString(), c.getDescricao(), c.getListaEspera().toString(), c.getData(), c.getStatus(), c.getIdPacienteChamado());
    }
}
