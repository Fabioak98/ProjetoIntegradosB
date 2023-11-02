package com.example.HealthCare.domain.profissional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfissionalRepository extends MongoRepository<Profissional,String> {
    @Query("{'ativo': true}")
    Page<Profissional> findAllByAtivoTrue(Pageable paginacao);

    Page<Profissional> findByEspecialidadeAndAtivoTrue(String especialidade,Pageable page);
    Page<Profissional> findByEndereco_CidadeAndAtivoTrue(String cidade,Pageable page);

    Page<Profissional> findByEspecialidadeAndEndereco_CidadeAndAtivoTrue(String especialidade, String cidade,Pageable page);
    Profissional findByEmail(String login);
}
