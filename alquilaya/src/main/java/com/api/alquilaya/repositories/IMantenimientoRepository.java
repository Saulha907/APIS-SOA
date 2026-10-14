package com.api.alquilaya.repositories;
import com.api.alquilaya.models.MantenimientoModel;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IMantenimientoRepository extends JpaRepository<MantenimientoModel,Long> {
}
