package com.example.HealthCare.domain.profissional;

import com.example.HealthCare.domain.avaliacao.Avaliacao;
import com.example.HealthCare.domain.consulta.Consulta;
import com.example.HealthCare.domain.endereco.Endereco;

import java.util.List;

public record DadosDetalhamentoProfissional(String id, String nome, String email, String crm,
                                            String telefone, Especialidade especialidade, Endereco endereco,
                                            Avaliacao avaliacao, String biografia, String urlFoto, Double valor) {
    public DadosDetalhamentoProfissional(Profissional profissional){
        this(profissional.getId(), profissional.getNome(), profissional.getEmail(), profissional.getCrm(),profissional.getTelefone(),
                profissional.getEspecialidade(),profissional.getEndereco(),profissional.getAvaliacao(), profissional.getBiografia(),
                profissional.getFoto(),profissional.getValor());
    }
}
