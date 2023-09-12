package com.example.HealthCare.domain.profissional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public record DadosCrudPerfil(
        @NotBlank
        String id,
        @NotNull
        Map<DayOfWeek,List<String>> horarios) {
}
