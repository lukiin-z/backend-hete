package com.fiap.ec.backend_consultas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fiap.ec.backend_consultas.model.Consulta;
import com.fiap.ec.backend_consultas.model.StatusConsulta;

public record ConsultaResponse(
        Long id,
        MedicoResumo medico,
        PacienteResumo paciente,
        LocalDateTime dataHora,
        StatusConsulta status,
        BigDecimal valor,
        String observacoes) {

    public static ConsultaResponse from(Consulta consulta) {
        return new ConsultaResponse(
                consulta.getId(),
                new MedicoResumo(
                        consulta.getMedico().getId(),
                        consulta.getMedico().getNome(),
                        consulta.getMedico().getCrm(),
                        new EspecialidadeResumo(
                                consulta.getMedico().getEspecialidade().getId(),
                                consulta.getMedico().getEspecialidade().getNome()),
                        consulta.getMedico().getAtivo()),
                new PacienteResumo(
                        consulta.getPaciente().getId(),
                        consulta.getPaciente().getNome(),
                        consulta.getPaciente().getCpf(),
                        consulta.getPaciente().getEmail(),
                        consulta.getPaciente().getTelefone(),
                        consulta.getPaciente().getDataNascimento(),
                        consulta.getPaciente().getAtivo()),
                consulta.getDataHora(),
                consulta.getStatus(),
                consulta.getValor(),
                consulta.getObservacoes());
    }

    public record EspecialidadeResumo(Long id, String nome) {
    }

    public record MedicoResumo(
            Long id,
            String nome,
            String crm,
            EspecialidadeResumo especialidade,
            Boolean ativo) {
    }

    public record PacienteResumo(
            Long id,
            String nome,
            String cpf,
            String email,
            String telefone,
            LocalDate dataNascimento,
            Boolean ativo) {
    }
}
