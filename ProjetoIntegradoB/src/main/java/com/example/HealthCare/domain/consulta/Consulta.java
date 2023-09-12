package com.example.HealthCare.domain.consulta;

import com.example.HealthCare.domain.paciente.Paciente;
import com.example.HealthCare.domain.profissional.Profissional;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Consulta {
    @DBRef
    private Profissional profissional;
    private Paciente paciente;
    private LocalDateTime data;
    private String descricao;


}
