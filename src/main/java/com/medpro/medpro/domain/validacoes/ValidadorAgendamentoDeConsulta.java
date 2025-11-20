package com.medpro.medpro.domain.validacoes;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.medpro.medpro.infra.execption.ValidacaoException;
import com.medpro.medpro.model.dto.DadosAgendamentoConsulta;
import com.medpro.medpro.repository.ConsultaRepository;
import com.medpro.medpro.repository.PacienteRepository;

public interface ValidadorAgendamentoDeConsulta {

    void validar(DadosAgendamentoConsulta dados);

    @Component
    public class ValidadorHorarioFuncionamento implements ValidadorAgendamentoDeConsulta {
        public void validar(DadosAgendamentoConsulta dados) {
            var dataConsulta = dados.data();
            var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
            var antesDaAbertura = dataConsulta.getHour() < 7;
            var depoisDoFechamento = dataConsulta.getHour() > 18;

            if (domingo || antesDaAbertura || depoisDoFechamento) {
                throw new ValidacaoException("Consulta fora do horário de funcionamento da clínica");
            }
        }
    }

    @Component
    public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoDeConsulta {
        public void validar(DadosAgendamentoConsulta dados) {
            var dataConsulta = dados.data();
            var agora = LocalDateTime.now();
            var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();

            if (diferencaEmMinutos < 30) {
                throw new ValidacaoException("Consulta deve ser agendada com antecedência mínima de 30 minutos");
            }
        }
    }

    @Component
    public class ValidadorPacienteAtivo implements ValidadorAgendamentoDeConsulta {
        @Autowired
        private PacienteRepository repository;

        public void validar(DadosAgendamentoConsulta dados) {
            var pacienteEstaAtivo = repository.findAtivoById(dados.idPaciente());

            if (!pacienteEstaAtivo) {
                throw new ValidacaoException("Consulta não pode ser agendada com paciente excluído");
            }
        }
    }

    @Component
    public class ValidadorMedicoComOutraConsultaNoMesmoHorario implements ValidadorAgendamentoDeConsulta {
        @Autowired
        private ConsultaRepository repository;

        public void validar(DadosAgendamentoConsulta dados) {
            var medicoPossuiOutraConsultaNoMesmoHorario = repository.existsByMedicoIdAndData(dados.idMedico(),
                    dados.data());
            if (medicoPossuiOutraConsultaNoMesmoHorario) {
                throw new ValidacaoException("Médico já possui outra consulta agendada nesse mesmo horário");
            }
        }
    }

    @Component
    public class ValidadorPacienteSemOutraConsultaNoDia implements ValidadorAgendamentoDeConsulta {
        @Autowired
        private ConsultaRepository repository;

        public void validar(DadosAgendamentoConsulta dados) {
            var primeiroHorario = dados.data().withHour(7);
            var ultimoHorario = dados.data().withHour(18);
            var pacientePossuiOutraConsultaNoDia = repository.existsByPacienteIdAndDataBetween(dados.idPaciente(),
                    primeiroHorario, ultimoHorario);
            if (pacientePossuiOutraConsultaNoDia) {
                throw new ValidacaoException("Paciente já possui uma consulta agendada nesse dia");
            }
        }
    }
}
