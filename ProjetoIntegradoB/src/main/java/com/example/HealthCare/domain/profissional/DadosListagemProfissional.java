package com.example.HealthCare.domain.profissional;

import com.example.HealthCare.domain.avaliacao.Avaliacao;

public record DadosListagemProfissional(String id, String nome, String email, String crm, Especialidade especialidade,
                                        Avaliacao avaliacao) {
    public DadosListagemProfissional(Profissional profissional){
        this(profissional.getId(),profissional.getNome(),profissional.getEmail(),profissional.getCrm(),profissional.getEspecialidade(),profissional.getAvaliacao());
    }
}
