package com.example.HealthCare.domain.profissional;

import com.example.HealthCare.domain.avaliacao.Avaliacao;

public record DadosListagemProfissional(String id, String nome, String email, String crm, Especialidade especialidade,
                                        String biografia, Avaliacao avaliacao, String urlFoto, Double valor) {
    public DadosListagemProfissional(Profissional profissional){
        this(profissional.getId(),profissional.getNome(),profissional.getEmail(),profissional.getCrm(),
                profissional.getEspecialidade(),profissional.getBiografia(),profissional.getAvaliacao(),
                profissional.getFoto(),profissional.getValor());
    }
}
