package com.example.HealthCare.domain.profissional;

import com.example.HealthCare.domain.avaliacao.Avaliacao;
import com.example.HealthCare.domain.consulta.Consulta;
import com.example.HealthCare.domain.endereco.Endereco;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
    private Avaliacao avaliacao;
    private String biografia;
    private String foto;
    private Double valor;
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
        this.avaliacao = new Avaliacao();
        this.biografia = "";
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
        if(dados.horarios() != null){
            atualizaAgenda(dados.horarios());
        }
        if(dados.biografia() != null){
            this.biografia = dados.biografia();
        }
        if(dados.valor() != null){
            this.valor = dados.valor();
        }
    }

    public void atualizaAgenda(Map<DayOfWeek, List<String>> dados){
        for(DayOfWeek day: dados.keySet()){
            this.agenda.put(day, dados.get(day));
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

    public void removerConsulta(Consulta consulta) throws Exception {
        if(consulta.getProfissional() == this){
            this.consultas.remove(consulta);
        }
        else{
            throw new Exception();
        }
    }

    public void addConsulta(Consulta consulta) {
        this.consultas.add(consulta);
    }

    public void removeConsulta(Consulta consulta){
        this.consultas.remove(consulta);
    }

    public void novaNota(int nota){
        this.avaliacao.novaNota(nota);
    }

    @Override
    public String toString() {
        return "Nome: " + this.nome + " id:"+ this.id;
    }
}
