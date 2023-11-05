package com.example.HealthCare.domain.consulta;

import com.example.HealthCare.domain.paciente.Paciente;
import com.example.HealthCare.domain.paciente.PacienteRepository;
import com.example.HealthCare.domain.profissional.ProfissionalRepository;
import com.example.HealthCare.domain.twilio.TwilioService;
import com.example.HealthCare.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@EnableAsync
public class ReagendamentoManager {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TwilioService twilioService;

    @Async
    public void chamaListaEspera(String idConsulta) throws InterruptedException {
        System.out.println("Thread Criada" + Thread.currentThread());
        var consulta = consultaRepository.findById(idConsulta).get();
        var profissional = consulta.getProfissional();
        System.out.println(consulta.getListaEspera());
        while (!consulta.getListaEspera().isEmpty()){

            System.out.println("Entro no while");
            var paciente = consulta.chamaPaciente();
            consulta = consultaRepository.save(consulta);
            System.out.println(paciente);
            consulta = chamaPaciente(paciente, consulta);
            if(consulta.getStatus() == Status.CONFIRMADO){
                System.out.println("Reagendou");
                break;
            }
        }
        if(consulta.getStatus() == Status.REAGENDANDO){
            profissional.removeConsulta(consulta);
            profissionalRepository.save(profissional);
            consultaRepository.delete(consulta);
            System.out.println("Deleto Consulta");
        }
    }

    public Consulta chamaPaciente(Paciente paciente,Consulta c) throws InterruptedException {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(10);
        var token = tokenService.gerarToken(paciente.getEmail(),end.toInstant(ZoneOffset.of("-03:00")));
        String mensagem = montaMensagem(paciente,c);
        System.out.println(mensagem);
        twilioService.enviaMensagem(mensagem, "+5513996681010");
        var consulta = c;
        boolean verifica = true;
        while(start.isBefore(end)){
            if(consulta.getStatus() == Status.CONFIRMADO){
                System.out.println("REAGENDOU");
                return consulta;
            }
            System.out.println("Thread Criada" + Thread.currentThread());
            Thread.sleep(1000);
            consulta = consultaRepository.findById(consulta.getId()).get();
            start = LocalDateTime.now();
            if(Duration.between(start,end).toMinutes() < 5 && verifica){
                verifica = false;
                twilioService.enviaMensagem("Restam 5 minutos");
            }
        }
        return consulta;
    }

    private String montaMensagem(Paciente paciente, Consulta consulta) {
        LocalDate diaC = LocalDate.of(consulta.getData().getYear(), consulta.getData().getMonthValue(), consulta.getData().getDayOfMonth());


        int horasC = consulta.getData().getHour();
        int minutosC = consulta.getData().getMinute();

        var consultaR = paciente.getConsultas().stream().filter(c -> c.getProfissional().getId().equals(consulta.getProfissional().getId())).findFirst().orElse(null);
        if(consultaR != null) {
            LocalDate diaR = LocalDate.of(consultaR.getData().getYear(), consultaR.getData().getMonthValue(), consultaR.getData().getDayOfMonth());
            int horasR = consultaR.getData().getHour();
            int minutosR = consultaR.getData().getMinute();
            String url = "http://healthycare.localhost:3000/reagendamento/" + consulta.getId() + "/" + consultaR.getId();
            return "Deseja reagenda sua consulta do dia " + diaR + " as " + String.format("%02d",horasR) + ":" + String.format("%02d",minutosR) +
                    " para o dia" + diaC + " as " + String.format("%02d",horasC) + ":" + String.format("%02d",minutosC) + "? Para confirmar acesse o link: " + url;
        }
        return null;
    }
}
