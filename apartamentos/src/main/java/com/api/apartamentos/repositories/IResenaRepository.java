
package com.api.apartamentos.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api.apartamentos.models.ResenaModel;

public interface IResenaRepository extends JpaRepository<ResenaModel, Long> {

    List<ResenaModel> findByReserva_IdReservacion(Long idReservacion);

    boolean existsByReserva_IdReservacionAndIdResenaNot(
        Long idReservacion,
        Long idResena
    );

    boolean existsByReserva_IdReservacion(Long idReservacion);
}
