package com.example.HealthCare.domain.profissional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ProfissionalRepository extends MongoRepository<Profissional,String> {
    @Query("{'ativo': true}")
    Page<Profissional> findAllByAtivoTrue(Pageable paginacao);

    Profissional findByEmail(String login);
}
