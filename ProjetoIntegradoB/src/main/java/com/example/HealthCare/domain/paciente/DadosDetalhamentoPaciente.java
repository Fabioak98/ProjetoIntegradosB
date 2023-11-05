package com.example.HealthCare.domain.paciente;

import com.example.HealthCare.domain.endereco.Endereco;

import java.util.List;

public record DadosDetalhamentoPaciente(String nome, String email, String telefone, String cpf, Endereco endereco, String urlFoto) {
    public DadosDetalhamentoPaciente(Paciente paciente){
        this(paciente.getNome(), paciente.getEmail(), paciente.getTelefone(), paciente.getCpf(), paciente.getEndereco(), paciente.getFoto());
    }
}
