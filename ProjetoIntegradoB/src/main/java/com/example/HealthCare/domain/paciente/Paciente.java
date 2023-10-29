package com.example.HealthCare.domain.paciente;


import com.example.HealthCare.domain.consulta.Consulta;
import com.example.HealthCare.domain.endereco.Endereco;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Paciente {
    @Id
    private String id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private Endereco endereco;
    private Boolean ativo;
    @DBRef
    private List<Consulta> consultas;

    public Paciente(DadosCadastroPaciente dados){
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cpf = dados.cpf();
        this.endereco = new Endereco(dados.endereco());
        this.ativo = true;
        this.consultas = new ArrayList<Consulta>();
    }

    public void excluir() {
        this.ativo = false;
    }

    public void addConsulta(Consulta consulta) {
        this.consultas.add(consulta);
    }

    public void removeConsulta(Consulta consulta) {
        if(this.consultas.contains(consulta)){
            this.consultas.remove(consulta);
        }
        else
            throw new  IllegalSelectorException();
    }

    @Override
    public String toString() {
        return "Nome: " + this.nome + " id:"+ this.id;
    }
}
