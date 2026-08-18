package com.fiap.ec.backend_consultas.service;

import com.fiap.ec.backend_consultas.model.Especialidade;
import com.fiap.ec.backend_consultas.repository.EspecialidadeRepository;
import com.fiap.ec.backend_consultas.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadeService {

    private final EspecialidadeRepository repository;

    public EspecialidadeService(EspecialidadeRepository repository) {
        this.repository = repository;
    }

    public Especialidade salvar(Especialidade especialidade) {
        return repository.save(especialidade);
    }

    public List<Especialidade> listar() {
        return repository.findAll();
    }

    public Especialidade buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade", id));
    }

    public Especialidade atualizar(Long id, Especialidade especialidadeAtualizada) {
        Especialidade especialidadeExistente = buscarPorId(id);
        especialidadeExistente.setNome(especialidadeAtualizada.getNome());
        especialidadeExistente.setDescricao(especialidadeAtualizada.getDescricao());
        return repository.save(especialidadeExistente);
    }

    public void deletar(Long id) {
        Especialidade especialidade = buscarPorId(id);
        repository.delete(especialidade);
    }
}
