package com.example.HealthCare.domain.profissional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PerfilProfissional {
    private DayOfWeek diasdeAtendimento;
    private List<String> horarios;


    public PerfilProfissional(DadosCrudPerfil dados){

    }
}
