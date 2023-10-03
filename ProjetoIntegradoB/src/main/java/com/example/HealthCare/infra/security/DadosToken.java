package com.example.HealthCare.infra.security;

import com.example.HealthCare.domain.usuario.Tipo;

public record DadosToken(String id, String token, Tipo tipo) {
}
