package com.fiap.ec.backend_consultas.controller;

import com.fiap.ec.backend_consultas.model.Medico;
import com.fiap.ec.backend_consultas.service.MedicoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Medico> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Medico buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Medico salvar(@Valid @RequestBody Medico medico) {
        return service.salvar(medico);
    }

    @PutMapping("/{id}")
    public Medico atualizar(@PathVariable Long id, @Valid @RequestBody Medico medico) {
        return service.atualizar(id, medico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
