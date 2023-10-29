package com.example.HealthCare.domain.consulta;

import jakarta.validation.constraints.NotBlank;

public record DadosCancelamentoConsulta(
        @NotBlank
        String idConsulta,
        @NotBlank
        String idPaciente,
        @NotBlank
        String idProf) {
}
