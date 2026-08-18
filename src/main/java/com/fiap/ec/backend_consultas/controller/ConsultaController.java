package com.fiap.ec.backend_consultas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import com.fiap.ec.backend_consultas.model.Consulta;
import com.fiap.ec.backend_consultas.service.ConsultaService;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Consulta> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Consulta buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Consulta criar(@Valid @RequestBody Consulta consulta) {
        return service.salvar(consulta);
    }

    @PutMapping("/{id}")
    public Consulta atualizar(@PathVariable Long id, @Valid @RequestBody Consulta consulta) {
        return service.atualizar(id, consulta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medico/{medicoId}")
    public List<Consulta> listarPorMedico(@PathVariable Long medicoId) {
        return service.listarPorMedico(medicoId);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<Consulta> listarPorPaciente(@PathVariable Long pacienteId) {
        return service.listarPorPaciente(pacienteId);
    }
}
