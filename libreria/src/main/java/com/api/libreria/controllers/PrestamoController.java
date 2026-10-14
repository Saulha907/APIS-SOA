
package com.api.libreria.controllers;

import com.api.libreria.models.PrestamoModel;
import com.api.libreria.services.PrestamoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public List<PrestamoModel> obtenerPrestamos() {
        return prestamoService.obtenerPrestamos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoModel> obtenerPrestamoPorId(
            @PathVariable Long id) {

        return prestamoService.obtenerPrestamoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<PrestamoModel> obtenerPrestamosPorUsuario(
            @PathVariable Long usuarioId) {

        return prestamoService.obtenerPrestamosPorUsuario(usuarioId);
    }

    @GetMapping("/libro/{isbn}")
    public List<PrestamoModel> obtenerPrestamosPorLibro(
            @PathVariable Long isbn) {

        return prestamoService.obtenerPrestamosPorLibro(isbn);
    }

    @PostMapping
    public ResponseEntity<?> registrarPrestamo(
            @RequestParam Long usuarioId,
            @RequestParam Long isbn) {

        try {
            PrestamoModel prestamo =
                    prestamoService.registrarPrestamo(usuarioId, isbn);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(prestamo);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolverLibro(@PathVariable Long id) {

        try {
            PrestamoModel prestamo =
                    prestamoService.devolverLibro(id);

            return ResponseEntity.ok(prestamo);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
