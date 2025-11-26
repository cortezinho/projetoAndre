package com.medpro.medpro.domain.validacoes;

import com.medpro.medpro.infra.execption.ValidacaoException;
import com.medpro.medpro.model.dto.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoDeConsulta {

    public void validar(DadosAgendamentoConsulta dados) {
        var dataConsulta = dados.data();
        var hora = dataConsulta.getHour();
        var minuto = dataConsulta.getMinute();

<<<<<<< HEAD
        if (hora < 7 || hora >= 18) {
=======
        //Limita a marcação de consultas por horário
        if (hora < 7 || hora > 18) {
>>>>>>> 159213704427ba4ce5d7d047d73087da12d7cdc8
            throw new ValidacaoException("Consulta fora do horário de funcionamento da clínica (07:00 às 19:00)");
        }
        //Limitação em minutos, evitando que consultas que ultrapassem às 19:00 sejam marcadas
        if (hora == 18) {
            if (minuto != 0){
                throw new ValidacaoException("Consulta fora do horário de funcionamento da clínica (07:00 às 19:00)");
            }
        }


        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        if (domingo) {
            throw new ValidacaoException("A clínica não funciona aos domingos!");
        }
    }
}