package com.example.HealthCare.domain.profissional;
import com.example.HealthCare.domain.endereco.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroProfissional(
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String telefone,
        @NotBlank
        String crm,
        @NotNull
        Especialidade especialidade,
        @NotBlank
        String senha,
        @Valid
        DadosEndereco endereco){
}