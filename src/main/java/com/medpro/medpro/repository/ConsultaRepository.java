package com.medpro.medpro.repository;

import com.medpro.medpro.model.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("""
                    select count(c) > 0 from Consulta c
                    where c.medico.id = :idMedico
                    and c.data > :horarioInicio
                    and c.data < :horarioFim
            """)
    Boolean existsByMedicoIdAndDataConflict(Long idMedico, LocalDateTime horarioInicio, LocalDateTime horarioFim);

    Boolean existsByPacienteIdAndDataBetween(Long idPaciente, LocalDateTime primeiroHorario,
            LocalDateTime ultimoHorario);
}