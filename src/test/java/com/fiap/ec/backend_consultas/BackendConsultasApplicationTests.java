package com.fiap.ec.backend_consultas;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.ec.backend_consultas.dto.ConsultaCreateRequest;
import com.fiap.ec.backend_consultas.exception.BusinessRuleException;
import com.fiap.ec.backend_consultas.model.Consulta;
import com.fiap.ec.backend_consultas.model.Especialidade;
import com.fiap.ec.backend_consultas.model.Medico;
import com.fiap.ec.backend_consultas.model.Paciente;
import com.fiap.ec.backend_consultas.model.StatusConsulta;
import com.fiap.ec.backend_consultas.service.ConsultaService;
import com.fiap.ec.backend_consultas.service.EspecialidadeService;
import com.fiap.ec.backend_consultas.service.MedicoService;
import com.fiap.ec.backend_consultas.service.PacienteService;

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

    private Medico medico;
    private Paciente paciente;
    private LocalDateTime horario;

    @BeforeEach
    void prepararCadastros() {
        String sufixo = String.valueOf(System.nanoTime());
        Especialidade especialidade = especialidadeService.salvar(
                new Especialidade("Cardiologia " + sufixo, "Saúde cardiovascular"));
        medico = medicoService.salvar(
                new Medico(null, "Dra. Ana", "CRM-" + sufixo, especialidade, true));
        paciente = pacienteService.salvar(new Paciente(
                "João Silva",
                "CPF-" + sufixo.substring(Math.max(0, sufixo.length() - 10)),
                "joao-" + sufixo + "@example.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                true));
        horario = LocalDateTime.now().plusDays(2).withSecond(0).withNano(0);
    }

    @Test
    void deveAgendarConsultaComCadastrosAtivos() {
        Consulta consulta = criarConsulta(medico.getId(), paciente.getId(), horario);

        assertThat(consulta.getId()).isNotNull();
        assertThat(consulta.getStatus()).isEqualTo(StatusConsulta.AGENDADA);
        assertThat(consultaService.listarPorMedico(medico.getId())).containsExactly(consulta);
    }

    @Test
    void deveImpedirConflitoDeHorarioDoMedico() {
        criarConsulta(medico.getId(), paciente.getId(), horario);

        Paciente outroPaciente = pacienteService.salvar(new Paciente(
                "Maria Souza",
                "987.654.321-00",
                "maria@example.com",
                null,
                LocalDate.of(1985, 5, 20),
                true));

        assertThatThrownBy(() -> criarConsulta(medico.getId(), outroPaciente.getId(), horario))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("médico já possui");
    }

    @Test
    void deveRespeitarFluxoDeStatus() {
        Consulta consulta = criarConsulta(medico.getId(), paciente.getId(), horario);

        consultaService.atualizarStatus(consulta.getId(), StatusConsulta.CONFIRMADA);
        consultaService.atualizarStatus(consulta.getId(), StatusConsulta.REALIZADA);

        assertThatThrownBy(() ->
                consultaService.atualizarStatus(consulta.getId(), StatusConsulta.CANCELADA))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Transição de status inválida");
    }

    @Test
    void deveLocalizarCadastrosParaOsFluxosDeLogin() {
        assertThat(medicoService.buscarPorCrm(medico.getCrm().toLowerCase())).isEqualTo(medico);
        assertThat(medicoService.listarPorEspecialidade(medico.getEspecialidade().getId()))
                .contains(medico);
        assertThat(pacienteService.buscarPorCpf(paciente.getCpf())).isEqualTo(paciente);
    }

    private Consulta criarConsulta(Long medicoId, Long pacienteId, LocalDateTime dataHora) {
        return consultaService.criar(new ConsultaCreateRequest(
                medicoId,
                pacienteId,
                dataHora,
                new BigDecimal("250.00"),
                "Consulta de rotina"));
    }
}
