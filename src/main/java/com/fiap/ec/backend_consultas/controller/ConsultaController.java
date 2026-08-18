package com.fiap.ec.backend_consultas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.ec.backend_consultas.dto.ConsultaCreateRequest;
import com.fiap.ec.backend_consultas.dto.ConsultaResponse;
import com.fiap.ec.backend_consultas.dto.ConsultaUpdateRequest;
import com.fiap.ec.backend_consultas.dto.StatusUpdateRequest;
import com.fiap.ec.backend_consultas.service.ConsultaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @GetMapping
    public List<ConsultaResponse> listar() {
        return service.listar().stream().map(ConsultaResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ConsultaResponse buscarPorId(@PathVariable Long id) {
        return ConsultaResponse.from(service.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultaResponse criar(@Valid @RequestBody ConsultaCreateRequest request) {
        return ConsultaResponse.from(service.criar(request));
    }

    @PutMapping("/{id}")
    public ConsultaResponse atualizar(@PathVariable Long id,
                                      @Valid @RequestBody ConsultaUpdateRequest request) {
        return ConsultaResponse.from(service.atualizar(id, request));
    }

    @PatchMapping("/{id}/status")
    public ConsultaResponse atualizarStatus(@PathVariable Long id,
                                            @Valid @RequestBody StatusUpdateRequest request) {
        return ConsultaResponse.from(service.atualizarStatus(id, request.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medico/{medicoId}")
    public List<ConsultaResponse> listarPorMedico(@PathVariable Long medicoId) {
        return service.listarPorMedico(medicoId).stream().map(ConsultaResponse::from).toList();
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<ConsultaResponse> listarPorPaciente(@PathVariable Long pacienteId) {
        return service.listarPorPaciente(pacienteId).stream().map(ConsultaResponse::from).toList();
    }
}
