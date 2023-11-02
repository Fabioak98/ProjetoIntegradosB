package com.example.HealthCare.domain.profissional;

import com.example.HealthCare.domain.endereco.DadosEndereco;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public record DadosAtualizacaoProfissional(
        @NotNull
        String id,
        String nome,
        String telefone,
        DadosEndereco endereco,
        Map<DayOfWeek, List<String>> horarios,
        String biografia
){
}
