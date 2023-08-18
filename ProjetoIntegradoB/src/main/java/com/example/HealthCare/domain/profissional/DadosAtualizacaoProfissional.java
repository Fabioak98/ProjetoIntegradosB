package com.example.HealthCare.domain.profissional;

import com.example.HealthCare.domain.endereco.DadosEndereco;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoProfissional(
        @NotNull
        String id,
        String nome,
        String telefone,
        DadosEndereco endereco
){
}
