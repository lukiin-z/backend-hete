package com.fiap.ec.backend_consultas.dto;

import com.fiap.ec.backend_consultas.model.StatusConsulta;

import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(@NotNull StatusConsulta status) {
}
