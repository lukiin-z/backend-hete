package com.fiap.ec.backend_consultas.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;

public enum StatusConsulta {
    AGENDADA,
    CONFIRMADA,
    CANCELADA,
    REALIZADA;

    public boolean podeTransicionarPara(StatusConsulta destino) {
        if (this == destino) {
            return true;
        }

        return switch (this) {
            case AGENDADA -> Set.of(CONFIRMADA, CANCELADA).contains(destino);
            case CONFIRMADA -> Set.of(REALIZADA, CANCELADA).contains(destino);
            case CANCELADA, REALIZADA -> false;
        };
    }

    @JsonCreator
    public static StatusConsulta fromValue(String value) {
        return StatusConsulta.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}
