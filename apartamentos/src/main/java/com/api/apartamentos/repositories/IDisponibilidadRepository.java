
package com.api.apartamentos.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.apartamentos.models.DisponibilidadModel;

public interface IDisponibilidadRepository extends JpaRepository<DisponibilidadModel, Long> {

    List<DisponibilidadModel> findByUnidad_IdUnidad(Long idUnidad);

    @Query("""
        SELECT COUNT(d) > 0
        FROM DisponibilidadModel d
        WHERE d.unidad.idUnidad = :unidadId
        AND d.disponible = true
        AND d.fechaInicio <= :entrada
        AND d.fechaFin >= :salida
    """)
    boolean existeDisponibilidad(
        @Param("unidadId") Long unidadId,
        @Param("entrada") LocalDateTime entrada,
        @Param("salida") LocalDateTime salida
    );
}
