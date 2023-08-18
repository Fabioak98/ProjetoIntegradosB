package com.example.HealthCare.domain.profissional;

import com.example.HealthCare.domain.endereco.Endereco;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DayOfWeek;
import java.util.List;

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
    private PerfilProfissional perfil;

    public Profissional(DadosCadastroProfissional dados){
        this.nome= dados.nome();
        this.email = dados.email();
        this.crm = dados.crm();
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
        this.telefone = dados.telefone();
        this.ativo = true;
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

    public void excluir() {
        this.ativo = false;
    }
}
