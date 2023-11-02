package com.example.HealthCare.domain.profissional;

import com.example.HealthCare.domain.avaliacao.Avaliacao;

public record DadosListagemProfissional(String id, String nome, String email, String crm, Especialidade especialidade,
                                        String biografia, Avaliacao avaliacao) {
    public DadosListagemProfissional(Profissional profissional){
        this(profissional.getId(),profissional.getNome(),profissional.getEmail(),profissional.getCrm(),profissional.getEspecialidade(),profissional.getBiografia(),profissional.getAvaliacao());
    }
}
