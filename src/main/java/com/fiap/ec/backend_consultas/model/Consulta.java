package com.fiap.ec.backend_consultas.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusConsulta status = StatusConsulta.AGENDADA;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Size(max = 1000)
    @Column(length = 1000)
    private String observacoes;

    public Consulta() {
    }

    public Consulta(Medico medico, Paciente paciente, LocalDateTime dataHora,
                    StatusConsulta status, BigDecimal valor, String observacoes) {
        this.medico = medico;
        this.paciente = paciente;
        this.dataHora = dataHora;
        this.status = status;
        this.valor = valor;
        this.observacoes = observacoes;
    }

    public Long getId() {
        return id;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
