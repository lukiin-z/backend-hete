package com.fiap.ec.backend_consultas;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fiap.ec.backend_consultas.model.Consulta;
import com.fiap.ec.backend_consultas.model.Especialidade;
import com.fiap.ec.backend_consultas.model.Medico;
import com.fiap.ec.backend_consultas.model.Paciente;
import com.fiap.ec.backend_consultas.model.StatusConsulta;
import com.fiap.ec.backend_consultas.service.ConsultaService;
import com.fiap.ec.backend_consultas.service.EspecialidadeService;
import com.fiap.ec.backend_consultas.service.MedicoService;
import com.fiap.ec.backend_consultas.service.PacienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BackendConsultasApplicationTests {

	@Autowired
	private EspecialidadeService especialidadeService;

	@Autowired
	private MedicoService medicoService;

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private ConsultaService consultaService;

	@Test
	void deveAgendarConsultaComCadastrosExistentes() {
		Especialidade especialidade = especialidadeService.salvar(
				new Especialidade("Cardiologia", "Saúde cardiovascular"));
		Medico medico = medicoService.salvar(
				new Medico(null, "Dra. Ana", "CRM-12345", especialidade, true));
		Paciente paciente = pacienteService.salvar(new Paciente(
				"João Silva",
				"123.456.789-00",
				"joao@example.com",
				"11999999999",
				LocalDate.of(1990, 1, 1),
				true));

		Consulta consulta = consultaService.salvar(new Consulta(
				medico,
				paciente,
				LocalDateTime.of(2026, 8, 20, 10, 0),
				StatusConsulta.AGENDADA,
				new BigDecimal("250.00"),
				"Consulta de rotina"));

		assertThat(consulta.getId()).isNotNull();
		assertThat(consulta.getMedico().getId()).isEqualTo(medico.getId());
		assertThat(consulta.getPaciente().getId()).isEqualTo(paciente.getId());
		assertThat(consultaService.listarPorMedico(medico.getId())).containsExactly(consulta);
	}

}
