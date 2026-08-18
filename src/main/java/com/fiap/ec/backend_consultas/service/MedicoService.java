package com.fiap.ec.backend_consultas.service;

import com.fiap.ec.backend_consultas.model.Medico;
import com.fiap.ec.backend_consultas.model.Especialidade;
import com.fiap.ec.backend_consultas.repository.EspecialidadeRepository;
import com.fiap.ec.backend_consultas.repository.MedicoRepository;
import com.fiap.ec.backend_consultas.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MedicoService {
    private final MedicoRepository repository;
    private final EspecialidadeRepository especialidadeRepository;

    public MedicoService(MedicoRepository repository, EspecialidadeRepository especialidadeRepository) {
        this.repository = repository;
        this.especialidadeRepository = especialidadeRepository;
    }

    public List<Medico> listar() {
        return repository.findAll();
    }

    public Medico buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", id));
    }

    public Medico salvar(Medico medico) {
        medico.setEspecialidade(buscarEspecialidade(medico));
        return repository.save(medico);
    }

    public Medico atualizar(Long id, Medico medicoAtualizado) {
        Medico medicoExistente = buscarPorId(id);
        medicoExistente.setNome(medicoAtualizado.getNome());
        medicoExistente.setCrm(medicoAtualizado.getCrm());
        medicoExistente.setEspecialidade(buscarEspecialidade(medicoAtualizado));
        medicoExistente.setAtivo(medicoAtualizado.getAtivo());
        return repository.save(medicoExistente);
    }

    public void deletar(Long id) {
        Medico medico = buscarPorId(id);
        repository.delete(medico);
    }

    private Especialidade buscarEspecialidade(Medico medico) {
        if (medico.getEspecialidade() == null || medico.getEspecialidade().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "especialidade.id é obrigatório");
        }
        Long especialidadeId = medico.getEspecialidade().getId();
        return especialidadeRepository.findById(especialidadeId)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade", especialidadeId));
    }
}
