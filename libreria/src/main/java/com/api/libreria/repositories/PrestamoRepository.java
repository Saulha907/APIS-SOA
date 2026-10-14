
package com.api.libreria.repositories;

import com.api.libreria.models.PrestamoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<PrestamoModel, Long> {

    List<PrestamoModel> findByUsuario_Id(Long usuarioId);

    List<PrestamoModel> findByLibro_Isbn(Long isbn);

    List<PrestamoModel> findByLibro_IsbnAndFechaDevolucionIsNull(Long isbn);
}
