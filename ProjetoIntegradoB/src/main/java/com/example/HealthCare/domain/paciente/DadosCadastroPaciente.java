package com.example.HealthCare.domain.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import com.example.HealthCare.domain.endereco.*;

public record DadosCadastroPaciente(
        @NotBlank
        String nome,
        @Email
        String email,
        @NotBlank
        @Pattern(regexp = "\\d{11}")
        String telefone,
        @NotBlank
        @Pattern(regexp = "\\d{11}")
        String cpf,
        @Valid
        DadosEndereco endereco){
}
