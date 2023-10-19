package com.example.HealthCare.domain.consulta;

import com.example.HealthCare.domain.paciente.Paciente;
import com.example.HealthCare.domain.profissional.Profissional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ConsultaRepository extends MongoRepository<Consulta,String> {
    @Query("{'data' : {$eq:  ?0}, 'medico': {$eq: ?1}}")
    Consulta findExistProf(LocalDateTime data, String medicoId);
    @Query("{'data' : {$eq:  ?0}, 'paciente': {$eq: ?1}}")
    Consulta findExistPac(LocalDateTime data, String PacitenId);
    @Query("{'data':  {$gte: ?0, $lt:  ?1} }")
    Consulta findByProf(String idProf,LocalDateTime data,LocalDateTime dataA);

    List<Consulta> findByDataBetweenAndProfissional(Instant de, Instant ate,Profissional profissional);
}
