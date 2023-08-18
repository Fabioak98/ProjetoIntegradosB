package com.example.HealthCare.infra.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {
    private record DadosErroValidacao(String campo,String mensagem){
        public DadosErroValidacao(FieldError error){
            this(error.getField(),error.getDefaultMessage());
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tartarErro400(MethodArgumentNotValidException ex){
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }
}
