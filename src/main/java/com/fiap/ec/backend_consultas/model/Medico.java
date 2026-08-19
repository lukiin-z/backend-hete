package com.fiap.ec.backend_consultas.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "medicos")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String nome;
    @NotBlank
    @Column(nullable = false, unique = true)
    private String crm;
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "especialidade_id", nullable = false)
    private Especialidade especialidade;
    @Column(nullable = false)
    @NotNull
    private Boolean ativo = true;
    @DecimalMin("0.0")
    @Column(precision = 10, scale = 2)
    private BigDecimal valorConsulta;

    public Medico() {
    }

    public Medico(Long id, String nome, String crm, Especialidade especialidade, Boolean ativo) {
        this(id, nome, crm, especialidade, ativo, null);
    }

    public Medico(Long id, String nome, String crm, Especialidade especialidade,
                  Boolean ativo, BigDecimal valorConsulta) {
        this.id = id;
        this.nome = nome;
        this.crm = crm;
        this.especialidade = especialidade;
        this.ativo = ativo;
        this.valorConsulta = valorConsulta;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCrm() {
        return crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public BigDecimal getValorConsulta() {
        return valorConsulta;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setValorConsulta(BigDecimal valorConsulta) {
        this.valorConsulta = valorConsulta;
    }
}
