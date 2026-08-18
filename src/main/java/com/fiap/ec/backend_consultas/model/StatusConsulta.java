package com.fiap.ec.backend_consultas.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusConsulta {
    AGENDADA,
    CONFIRMADA,
    CANCELADA,
    REALIZADA;

    @JsonCreator
    public static StatusConsulta fromValue(String value) {
        return StatusConsulta.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}
