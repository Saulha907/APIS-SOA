
package com.api.libreria.services;

import com.api.libreria.models.LibroModel;
import com.api.libreria.models.UsuarioModel;
import com.api.libreria.models.PrestamoModel;

import com.api.libreria.repositories.ILibroRepository;
import com.api.libreria.repositories.UsuarioRepository;
import com.api.libreria.repositories.PrestamoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final ILibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;

    public PrestamoService(
            PrestamoRepository prestamoRepository,
            ILibroRepository libroRepository,
            UsuarioRepository usuarioRepository) {

        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<PrestamoModel> obtenerPrestamos() {
        return prestamoRepository.findAll();
    }

    public Optional<PrestamoModel> obtenerPrestamoPorId(Long id) {
        return prestamoRepository.findById(id);
    }

    public List<PrestamoModel> obtenerPrestamosPorUsuario(Long usuarioId) {
        return prestamoRepository.findByUsuario_Id(usuarioId);
    }

    public List<PrestamoModel> obtenerPrestamosPorLibro(Long isbn) {
        return prestamoRepository.findByLibro_Isbn(isbn);
    }

    @Transactional
    public PrestamoModel registrarPrestamo(
            Long usuarioId, Long isbn) {

        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario no existe"));

        LibroModel libro = libroRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El libro no existe"));

        if (libro.getNumeroEjemplares() <= 0) {
            throw new IllegalArgumentException(
                    "No hay ejemplares disponibles");
        }

        libro.setNumeroEjemplares(
                libro.getNumeroEjemplares() - 1);

        libroRepository.save(libro);

        PrestamoModel prestamo = new PrestamoModel();

        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucion(null);

        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public PrestamoModel devolverLibro(Long prestamoId) {

        PrestamoModel prestamo = prestamoRepository
                .findById(prestamoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El prestamo no existe"));

        if (prestamo.getFechaDevolucion() != null) {
            throw new IllegalArgumentException(
                    "Este libro ya fue devuelto");
        }

        
        prestamo.setFechaDevolucion(LocalDate.now());

        LibroModel libro = prestamo.getLibro();

        libro.setNumeroEjemplares(
                libro.getNumeroEjemplares() + 1);

        libroRepository.save(libro);

        return prestamoRepository.save(prestamo);
    }
}
