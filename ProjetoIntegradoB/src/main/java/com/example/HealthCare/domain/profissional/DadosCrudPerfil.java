package com.example.HealthCare.domain.profissional;

import java.time.DayOfWeek;
import java.util.List;

public record DadosCrudPerfil(String id,List<DayOfWeek> dias, List<String> horarios) {
}
