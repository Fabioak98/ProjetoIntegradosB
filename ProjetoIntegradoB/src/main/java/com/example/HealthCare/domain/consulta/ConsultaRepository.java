package com.example.HealthCare.domain.consulta;

import com.example.HealthCare.domain.paciente.Paciente;
import com.example.HealthCare.domain.profissional.Profissional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;

public interface ConsultaRepository extends MongoRepository<Consulta,String> {
    @Query("{'data' : {$eq:  ?0}, 'medico': {$eq: ?1}}")
    Consulta findExistProf(LocalDateTime data, String medicoId);
    @Query("{'data' : {$eq:  ?0}, 'paciente': {$eq: ?1}}")
    Consulta findExistPac(LocalDateTime data, String PacitenId);
}
