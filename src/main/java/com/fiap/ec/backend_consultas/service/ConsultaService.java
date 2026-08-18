package com.fiap.ec.backend_consultas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fiap.ec.backend_consultas.model.Consulta;
import com.fiap.ec.backend_consultas.model.Medico;
import com.fiap.ec.backend_consultas.model.Paciente;
import com.fiap.ec.backend_consultas.exception.ResourceNotFoundException;
import com.fiap.ec.backend_consultas.repository.ConsultaRepository;
import com.fiap.ec.backend_consultas.repository.MedicoRepository;
import com.fiap.ec.backend_consultas.repository.PacienteRepository;

@Service
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
        return consultaRepository.findAll();
    }

    public Consulta buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", id));
    }

    public Consulta salvar(Consulta consulta) {
        validarRelacionamentos(consulta);
        Medico medico = medicoRepository.findById(consulta.getMedico().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico", consulta.getMedico().getId()));
        Paciente paciente = pacienteRepository.findById(consulta.getPaciente().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", consulta.getPaciente().getId()));

        consulta.setMedico(medico);
        consulta.setPaciente(paciente);

        return consultaRepository.save(consulta);
    }

    public Consulta atualizar(Long id, Consulta consultaAtualizada) {
        Consulta consultaExistente = buscarPorId(id);

        if (consultaAtualizada.getDataHora() != null) {
            consultaExistente.setDataHora(consultaAtualizada.getDataHora());
        }
        if (consultaAtualizada.getStatus() != null) {
            consultaExistente.setStatus(consultaAtualizada.getStatus());
        }
        if (consultaAtualizada.getValor() != null) {
            consultaExistente.setValor(consultaAtualizada.getValor());
        }
        consultaExistente.setObservacoes(consultaAtualizada.getObservacoes());

        if (consultaAtualizada.getMedico() != null && consultaAtualizada.getMedico().getId() != null) {
            Medico medico = medicoRepository.findById(consultaAtualizada.getMedico().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Médico", consultaAtualizada.getMedico().getId()));
            consultaExistente.setMedico(medico);
        }
        if (consultaAtualizada.getPaciente() != null && consultaAtualizada.getPaciente().getId() != null) {
            Paciente paciente = pacienteRepository.findById(consultaAtualizada.getPaciente().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente", consultaAtualizada.getPaciente().getId()));
            consultaExistente.setPaciente(paciente);
        }

        return consultaRepository.save(consultaExistente);
    }

    public void deletar(Long id) {
        Consulta consulta = buscarPorId(id);
        consultaRepository.delete(consulta);
    }

    public List<Consulta> listarPorMedico(Long medicoId) {
        return consultaRepository.findByMedicoId(medicoId);
    }

    public List<Consulta> listarPorPaciente(Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId);
    }

    private void validarRelacionamentos(Consulta consulta) {
        if (consulta.getMedico() == null || consulta.getMedico().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "medico.id é obrigatório");
        }
        if (consulta.getPaciente() == null || consulta.getPaciente().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "paciente.id é obrigatório");
        }
    }
}
