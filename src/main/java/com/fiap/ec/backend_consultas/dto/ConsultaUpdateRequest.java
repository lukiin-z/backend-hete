package com.fiap.ec.backend_consultas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fiap.ec.backend_consultas.model.StatusConsulta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

public record ConsultaUpdateRequest(
        Long medicoId,
        Long pacienteId,
        @Future LocalDateTime dataHora,
        StatusConsulta status,
        @DecimalMin("0.0") BigDecimal valor,
        @Size(max = 1000) String observacoes) {
}
