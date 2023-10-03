package com.example.HealthCare.domain.usuario;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosLogin(
        @JsonAlias("username")
        String login,
        @JsonAlias("password")
        String senha) {
}
