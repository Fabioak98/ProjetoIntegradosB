package com.example.HealthCare.domain.avaliacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Avaliacao {
    public int somaNotas;

    public int numAvaliacoes;

    public double notaMd;

    public Avaliacao(){
        this.somaNotas = 0;
        this.numAvaliacoes = 0;
        this.notaMd = 0;
    }

    public void novaNota(int nota){
        this.somaNotas += nota;
        this.numAvaliacoes +=1;
        this.notaMd = ((double) somaNotas /numAvaliacoes);

    }
}
