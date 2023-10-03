package com.example.HealthCare.domain.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAgendamentoConsulta(
        @NotBlank
        String idMedico,
        @NotBlank
        String idPaciente,
        @NotNull
        String descricao,
        @Future
        LocalDateTime data) {
}
