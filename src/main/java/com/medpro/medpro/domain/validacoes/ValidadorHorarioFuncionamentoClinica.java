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

        // Verifica se é antes das 07:00 ou depois das 18:00 (para fechar às 19:00)
        // Se a consulta começar às 19:00, ela terminaria às 20:00, o que estaria fora do horário.
        if (hora < 7 || hora > 18) {
            throw new ValidacaoException("Consulta fora do horário de funcionamento da clínica (07:00 às 19:00)");
        }

        // Opcional: Se desejar bloquear também os domingos
        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        if (domingo) {
            throw new ValidacaoException("A clínica não funciona aos domingos!");
        }
    }
}