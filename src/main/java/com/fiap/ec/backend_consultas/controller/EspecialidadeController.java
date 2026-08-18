package com.fiap.ec.backend_consultas.controller;

import com.fiap.ec.backend_consultas.model.Especialidade;
import com.fiap.ec.backend_consultas.service.EspecialidadeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    private final EspecialidadeService service;

    public EspecialidadeController(EspecialidadeService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Especialidade criar(@Valid @RequestBody Especialidade especialidade) {
        return service.salvar(especialidade);
    }

    @GetMapping
    public List<Especialidade> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Especialidade buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Especialidade atualizar(@PathVariable Long id, @Valid @RequestBody Especialidade especialidade) {
        return service.atualizar(id, especialidade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
