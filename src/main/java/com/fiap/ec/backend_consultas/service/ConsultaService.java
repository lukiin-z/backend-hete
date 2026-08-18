package com.fiap.ec.backend_consultas.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.ec.backend_consultas.dto.ConsultaCreateRequest;
import com.fiap.ec.backend_consultas.dto.ConsultaUpdateRequest;
import com.fiap.ec.backend_consultas.exception.BusinessRuleException;
import com.fiap.ec.backend_consultas.exception.ResourceNotFoundException;
import com.fiap.ec.backend_consultas.model.Consulta;
import com.fiap.ec.backend_consultas.model.Medico;
import com.fiap.ec.backend_consultas.model.Paciente;
import com.fiap.ec.backend_consultas.model.StatusConsulta;
import com.fiap.ec.backend_consultas.repository.ConsultaRepository;
import com.fiap.ec.backend_consultas.repository.MedicoRepository;
import com.fiap.ec.backend_consultas.repository.PacienteRepository;

@Service
@Transactional(readOnly = true)
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public ConsultaService(ConsultaRepository consultaRepository,
                           MedicoRepository medicoRepository,
                           PacienteRepository pacienteRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public List<Consulta> listar() {
        return consultaRepository.findAllByOrderByDataHoraAsc();
    }

    public Consulta buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", id));
    }

    @Transactional
    public Consulta criar(ConsultaCreateRequest request) {
        Medico medico = buscarMedicoAtivo(request.medicoId());
        Paciente paciente = buscarPacienteAtivo(request.pacienteId());
        validarHorarioDisponivel(medico.getId(), paciente.getId(), request.dataHora(), null);

        Consulta consulta = new Consulta(
                medico,
                paciente,
                request.dataHora(),
                StatusConsulta.AGENDADA,
                request.valor(),
                normalizarObservacoes(request.observacoes()));
        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta atualizar(Long id, ConsultaUpdateRequest request) {
        Consulta consulta = buscarPorId(id);

        Medico medico = request.medicoId() == null
                ? consulta.getMedico()
                : buscarMedicoAtivo(request.medicoId());
        Paciente paciente = request.pacienteId() == null
                ? consulta.getPaciente()
                : buscarPacienteAtivo(request.pacienteId());
        LocalDateTime dataHora = request.dataHora() == null
                ? consulta.getDataHora()
                : request.dataHora();

        validarHorarioDisponivel(medico.getId(), paciente.getId(), dataHora, consulta.getId());
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setDataHora(dataHora);

        if (request.status() != null) {
            aplicarStatus(consulta, request.status());
        }
        if (request.valor() != null) {
            consulta.setValor(request.valor());
        }
        if (request.observacoes() != null) {
            consulta.setObservacoes(normalizarObservacoes(request.observacoes()));
        }

        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta atualizarStatus(Long id, StatusConsulta novoStatus) {
        Consulta consulta = buscarPorId(id);
        aplicarStatus(consulta, novoStatus);
        return consultaRepository.save(consulta);
    }

    @Transactional
    public void deletar(Long id) {
        consultaRepository.delete(buscarPorId(id));
    }

    public List<Consulta> listarPorMedico(Long medicoId) {
        if (!medicoRepository.existsById(medicoId)) {
            throw new ResourceNotFoundException("Médico", medicoId);
        }
        return consultaRepository.findByMedicoIdOrderByDataHoraAsc(medicoId);
    }

    public List<Consulta> listarPorPaciente(Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new ResourceNotFoundException("Paciente", pacienteId);
        }
        return consultaRepository.findByPacienteIdOrderByDataHoraAsc(pacienteId);
    }

    private Medico buscarMedicoAtivo(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", id));
        if (!Boolean.TRUE.equals(medico.getAtivo())) {
            throw new BusinessRuleException("Não é possível agendar com um médico inativo");
        }
        return medico;
    }

    private Paciente buscarPacienteAtivo(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        if (!Boolean.TRUE.equals(paciente.getAtivo())) {
            throw new BusinessRuleException("Não é possível agendar para um paciente inativo");
        }
        return paciente;
    }

    private void validarHorarioDisponivel(Long medicoId, Long pacienteId,
                                          LocalDateTime dataHora, Long consultaId) {
        boolean medicoOcupado;
        boolean pacienteOcupado;

        if (consultaId == null) {
            medicoOcupado = consultaRepository.existsByMedicoIdAndDataHoraAndStatusNot(
                    medicoId, dataHora, StatusConsulta.CANCELADA);
            pacienteOcupado = consultaRepository.existsByPacienteIdAndDataHoraAndStatusNot(
                    pacienteId, dataHora, StatusConsulta.CANCELADA);
        } else {
            medicoOcupado = consultaRepository.existsByMedicoIdAndDataHoraAndStatusNotAndIdNot(
                    medicoId, dataHora, StatusConsulta.CANCELADA, consultaId);
            pacienteOcupado = consultaRepository.existsByPacienteIdAndDataHoraAndStatusNotAndIdNot(
                    pacienteId, dataHora, StatusConsulta.CANCELADA, consultaId);
        }

        if (medicoOcupado) {
            throw new BusinessRuleException("O médico já possui uma consulta nesse horário");
        }
        if (pacienteOcupado) {
            throw new BusinessRuleException("O paciente já possui uma consulta nesse horário");
        }
    }

    private void aplicarStatus(Consulta consulta, StatusConsulta novoStatus) {
        if (!consulta.getStatus().podeTransicionarPara(novoStatus)) {
            throw new BusinessRuleException(
                    "Transição de status inválida: "
                            + consulta.getStatus().toValue()
                            + " -> "
                            + novoStatus.toValue());
        }
        consulta.setStatus(novoStatus);
    }

    private String normalizarObservacoes(String observacoes) {
        if (observacoes == null || observacoes.isBlank()) {
            return null;
        }
        return observacoes.trim();
    }
}
