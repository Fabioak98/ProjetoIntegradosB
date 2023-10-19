package com.example.HealthCare.domain.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DadosAgendamentoConsulta(
        @NotBlank
        String idMedico,
        @NotBlank
        String idPaciente,
        @NotNull
        String descricao,
        @Future
        LocalDateTime data,
        List<LocalDate> diasLivres) {
}
