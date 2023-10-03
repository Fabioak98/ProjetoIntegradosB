package com.example.HealthCare.domain.usuario;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends MongoRepository<Usuario,String> {
    UserDetails findByLogin(String login);
    @Query("{'login': ?0}")
    Usuario findByEmail(String email);

}
