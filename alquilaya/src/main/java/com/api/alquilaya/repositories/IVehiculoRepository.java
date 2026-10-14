package com.api.alquilaya.repositories;
import com.api.alquilaya.models.VehiculoModel;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IVehiculoRepository extends JpaRepository<VehiculoModel,Long> {
}
