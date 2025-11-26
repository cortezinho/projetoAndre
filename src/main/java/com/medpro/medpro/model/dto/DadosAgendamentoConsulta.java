package com.medpro.medpro.model.dto;

import com.medpro.medpro.enums.Especialidade;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAgendamentoConsulta(
        @NotNull
        Long idMedico,

        @NotNull 
        Long idPaciente,

        @NotNull
        LocalDateTime data,

        Especialidade especialidade) {
}