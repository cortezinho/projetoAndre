package com.medpro.medpro.domain.validacoes;

import com.medpro.medpro.infra.execption.ValidacaoException;
import com.medpro.medpro.model.dto.DadosAgendamentoConsulta;
import com.medpro.medpro.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoComOutraConsultaNoMesmoHorario implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private ConsultaRepository repository;

    public void validar(DadosAgendamentoConsulta dados) {
        // Calcula o intervalo de conflito (1h antes e 1h depois)
        var horarioInicioConflict = dados.data().minusHours(1);
        var horarioFimConflict = dados.data().plusHours(1);

        // CORREÇÃO AQUI: Use o novo método "...Conflict" em vez do antigo
        var medicoPossuiOutraConsultaNoMesmoHorario = repository.existsByMedicoIdAndDataConflict(
                dados.idMedico(),
                horarioInicioConflict,
                horarioFimConflict);

        if (medicoPossuiOutraConsultaNoMesmoHorario) {
            throw new ValidacaoException("Médico já possui outra consulta agendada nesse mesmo horário");
        }
    }
}