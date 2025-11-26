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
<<<<<<< HEAD
        @Future
=======
>>>>>>> 159213704427ba4ce5d7d047d73087da12d7cdc8
        LocalDateTime data,

        Especialidade especialidade) {
}