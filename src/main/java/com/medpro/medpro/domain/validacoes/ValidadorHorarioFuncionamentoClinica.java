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

        if (hora < 7 || hora >= 18) {
            throw new ValidacaoException("Consulta fora do horário de funcionamento da clínica (07:00 às 19:00)");
        }

        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        if (domingo) {
            throw new ValidacaoException("A clínica não funciona aos domingos!");
        }
    }
}