package com.example.HealthCare.domain.twilio;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    public void enviaMensagem(String mensagem) {
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("whatsapp:+5513996681010"),
                        new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                        mensagem)
                .create();

        System.out.println(message.getSid());
    }
}
