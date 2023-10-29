package com.example.HealthCare.domain.consulta;

import jakarta.validation.constraints.NotBlank;

public record DadosReagendamentoConsulta(
        @NotBlank String idConsultaAT,
        @NotBlank String idConsultaRG,
        @NotBlank
        String idPaciente) {
}
