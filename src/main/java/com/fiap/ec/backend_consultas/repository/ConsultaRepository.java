package com.fiap.ec.backend_consultas.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiap.ec.backend_consultas.model.Consulta;
import com.fiap.ec.backend_consultas.model.StatusConsulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findAllByOrderByDataHoraAsc();

    List<Consulta> findByMedicoIdOrderByDataHoraAsc(Long medicoId);

    List<Consulta> findByPacienteIdOrderByDataHoraAsc(Long pacienteId);

    boolean existsByMedicoIdAndDataHoraAndStatusNot(Long medicoId, LocalDateTime dataHora, StatusConsulta status);

    boolean existsByPacienteIdAndDataHoraAndStatusNot(Long pacienteId, LocalDateTime dataHora, StatusConsulta status);

    boolean existsByMedicoIdAndDataHoraAndStatusNotAndIdNot(
            Long medicoId, LocalDateTime dataHora, StatusConsulta status, Long id);

    boolean existsByPacienteIdAndDataHoraAndStatusNotAndIdNot(
            Long pacienteId, LocalDateTime dataHora, StatusConsulta status, Long id);
}
