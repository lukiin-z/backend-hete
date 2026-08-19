package com.fiap.ec.backend_consultas.service;

import com.fiap.ec.backend_consultas.model.Paciente;
import com.fiap.ec.backend_consultas.repository.PacienteRepository;
import com.fiap.ec.backend_consultas.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {
    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public Paciente salvar(Paciente paciente) {
        paciente.setCpf(normalizarCpf(paciente.getCpf()));
        return repository.save(paciente);
    }

    public List<Paciente> listar() {
        return repository.findAll();
    }

    public Paciente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
    }

    public Paciente buscarPorCpf(String cpf) {
        String cpfNormalizado = normalizarCpf(cpf);
        return repository.findByCpf(cpfNormalizado)
                .orElseGet(() -> repository.findAll().stream()
                        .filter(paciente -> normalizarCpf(paciente.getCpf()).equals(cpfNormalizado))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Paciente com CPF " + cpf)));
    }

    public Paciente atualizar(Long id, Paciente pacienteAtualizado){
        Paciente pacienteExistente = buscarPorId(id);
        pacienteExistente.setNome(pacienteAtualizado.getNome());
        pacienteExistente.setCpf(normalizarCpf(pacienteAtualizado.getCpf()));
        pacienteExistente.setEmail(pacienteAtualizado.getEmail());
        pacienteExistente.setTelefone(pacienteAtualizado.getTelefone());
        pacienteExistente.setDataNascimento(pacienteAtualizado.getDataNascimento());
        pacienteExistente.setAtivo(pacienteAtualizado.getAtivo());
        return repository.save(pacienteExistente);
    }

    public void deletar(Long id){
        Paciente paciente = buscarPorId(id);
        repository.delete(paciente);
    }

    private String normalizarCpf(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("\\D", "");
    }
}
