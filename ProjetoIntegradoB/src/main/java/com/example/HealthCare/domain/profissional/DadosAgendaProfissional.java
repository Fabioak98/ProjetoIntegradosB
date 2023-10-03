package com.example.HealthCare.domain.profissional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public record DadosAgendaProfissional(Map<DayOfWeek, List<String>> agenda) {
}
