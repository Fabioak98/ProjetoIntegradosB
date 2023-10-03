package com.example.HealthCare.domain.consulta;

import com.example.HealthCare.domain.paciente.Paciente;
import com.example.HealthCare.domain.profissional.Profissional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Consulta {
    @Id
    private String id;
    @DBRef
    private Profissional profissional;
    @DBRef
    private Paciente paciente;
    private LocalDateTime data;
    private String descricao;
    @DBRef
    private List<Paciente> listaEspera;

    public Consulta(DadosAgendamentoConsulta dados){
        this.data = dados.data();
        this.descricao = dados.descricao();
        this.listaEspera = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Id: " + id + ", Data:" + data + ", Profissional:" + profissional.getNome();
    }
}
