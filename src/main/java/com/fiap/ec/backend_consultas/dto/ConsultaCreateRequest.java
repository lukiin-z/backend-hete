package com.fiap.ec.backend_consultas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ConsultaCreateRequest(
        @NotNull Long medicoId,
        @NotNull Long pacienteId,
        @NotNull @Future LocalDateTime dataHora,
        @NotNull @DecimalMin("0.0") BigDecimal valor,
        @Size(max = 1000) String observacoes) {
}
