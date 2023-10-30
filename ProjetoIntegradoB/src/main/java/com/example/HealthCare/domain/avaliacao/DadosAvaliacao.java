package com.example.HealthCare.domain.avaliacao;

import jakarta.validation.constraints.NotBlank;

public record DadosAvaliacao(
        @NotBlank
        String idProfissional,
        @NotBlank
        int nota) {
}
