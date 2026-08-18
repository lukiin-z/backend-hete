package com.fiap.ec.backend_consultas.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fiap.ec.backend_consultas.model.Consulta;
import com.fiap.ec.backend_consultas.model.Especialidade;
import com.fiap.ec.backend_consultas.model.Medico;
import com.fiap.ec.backend_consultas.model.Paciente;
import com.fiap.ec.backend_consultas.model.StatusConsulta;
import com.fiap.ec.backend_consultas.repository.ConsultaRepository;
import com.fiap.ec.backend_consultas.repository.EspecialidadeRepository;
import com.fiap.ec.backend_consultas.repository.MedicoRepository;
import com.fiap.ec.backend_consultas.repository.PacienteRepository;

@Component
@Profile("dev")
public class DevelopmentDataLoader implements CommandLineRunner {

    private final EspecialidadeRepository especialidadeRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;

    public DevelopmentDataLoader(EspecialidadeRepository especialidadeRepository,
                                 MedicoRepository medicoRepository,
                                 PacienteRepository pacienteRepository,
                                 ConsultaRepository consultaRepository) {
        this.especialidadeRepository = especialidadeRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.consultaRepository = consultaRepository;
    }

    @Override
    public void run(String... args) {
        if (especialidadeRepository.count() > 0
                || medicoRepository.count() > 0
                || pacienteRepository.count() > 0
                || consultaRepository.count() > 0) {
            return;
        }

        Especialidade cardiologia = especialidadeRepository.save(
                new Especialidade("Cardiologia", "Cuidados com o coração e sistema cardiovascular"));
        Especialidade dermatologia = especialidadeRepository.save(
                new Especialidade("Dermatologia", "Diagnóstico e tratamento da pele"));

        Medico ana = medicoRepository.save(
                new Medico(null, "Dra. Ana Martins", "CRM-SP 123456", cardiologia, true));
        Medico caio = medicoRepository.save(
                new Medico(null, "Dr. Caio Ribeiro", "CRM-SP 654321", dermatologia, true));

        Paciente joao = pacienteRepository.save(new Paciente(
                "João Silva",
                "123.456.789-00",
                "joao@example.com",
                "11999999999",
                LocalDate.of(1990, 1, 15),
                true));
        Paciente maria = pacienteRepository.save(new Paciente(
                "Maria Santos",
                "987.654.321-00",
                "maria@example.com",
                "11988888888",
                LocalDate.of(1987, 6, 10),
                true));

        LocalDateTime amanha = LocalDate.now().plusDays(1).atTime(9, 0);
        consultaRepository.saveAll(List.of(
                new Consulta(ana, joao, amanha, StatusConsulta.AGENDADA,
                        new BigDecimal("250.00"), "Consulta de rotina"),
                new Consulta(caio, maria, amanha.plusHours(5), StatusConsulta.CONFIRMADA,
                        new BigDecimal("320.00"), "Retorno pós-exame")));
    }
}
