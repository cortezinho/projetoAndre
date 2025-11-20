package com.medpro.medpro.model.dto;

import com.medpro.medpro.model.enums.MotivoCancelamento;
import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoConsulta(
        @NotNull
        Long idConsulta,

        @NotNull
        MotivoCancelamento motivo
) {
}