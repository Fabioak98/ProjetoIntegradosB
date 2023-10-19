package com.example.HealthCare.domain.profissional;

import com.example.HealthCare.domain.consulta.Consulta;
import com.example.HealthCare.domain.endereco.Endereco;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Profissional {

    @Id
    private String id;
    private String nome;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String crm;
    private Especialidade especialidade;
    private Endereco endereco;
    private String telefone;
    private Boolean ativo;
    private Map<DayOfWeek,List<String>> agenda;
    @DBRef
    private List<Consulta> consultas;

    public Profissional(DadosCadastroProfissional dados){
        this.nome= dados.nome();
        this.email = dados.email();
        this.crm = dados.crm();
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
        this.telefone = dados.telefone();
        this.agenda = inicialAgenda();
        this.consultas = new ArrayList<Consulta>();
        this.ativo = true;
    }

    private Map<DayOfWeek,List<String>> inicialAgenda() {
        Map<DayOfWeek,List<String>> hash = new HashMap<DayOfWeek,List<String>>();

        for(DayOfWeek day : DayOfWeek.values()){
            hash.put(day,null);
        }
        return hash;

    }


    public void atualizarInformacoes(DadosAtualizacaoProfissional dados) {
        if (dados.nome() != null){
            this.nome = dados.nome();
        }
        if(dados.telefone() != null){
            this.telefone = dados.telefone();
        }
        if(dados.endereco() != null){
            this.endereco.atualizarInformacoes(dados.endereco());
        }
    }

    public void atualizaAgenda(DadosCrudPerfil dadosCrudPerfil){
        for(DayOfWeek day: dadosCrudPerfil.horarios().keySet()){
            this.agenda.put(day,dadosCrudPerfil.horarios().get(day));
        }
    }

    public void excluir() {
        this.ativo = false;
    }

    public boolean validaHorario(LocalDateTime data){
        var diaSemana = data.getDayOfWeek();
        int horas = data.getHour();
        int minutos = data.getMinute();
        String horario = String.format("%02d:%02d",horas,minutos);
        var listaHorarios = this.agenda.get(diaSemana);

        if(listaHorarios == null){
            return false;
        }

        if(listaHorarios.contains(horario)){
            Consulta consulta = consultas.stream().filter(consulta1 -> consulta1.getData().equals(data)).findAny().orElse(null);
            if(consulta == null){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    public void addConsulta(Consulta consulta) {
        this.consultas.add(consulta);
    }
}
