package com.example.HealthCare.domain.profissional;

public record DadosListagemProfissional(String id,String nome, String email, String crm,Especialidade especialidade) {
    public DadosListagemProfissional(Profissional profissional){
        this(profissional.getId(),profissional.getNome(),profissional.getEmail(),profissional.getCrm(),profissional.getEspecialidade());
    }
}
