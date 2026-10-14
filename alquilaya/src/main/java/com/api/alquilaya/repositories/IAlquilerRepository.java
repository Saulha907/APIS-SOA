package com.api.alquilaya.repositories;
import com.api.alquilaya.models.AlquilerModel;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IAlquilerRepository extends JpaRepository<AlquilerModel,Long> {
 boolean existsByVehiculo_IdVehiculoAndEstadoInAndFechaAlquilerLessThanAndFechaDevolucionGreaterThan(Long id,java.util.Collection<String> estados,java.time.LocalDate salida,java.time.LocalDate entrada);

}
