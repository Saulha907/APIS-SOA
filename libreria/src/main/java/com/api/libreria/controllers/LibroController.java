
package com.api.libreria.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.api.libreria.models.LibroModel;
import com.api.libreria.services.LibroService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    public ArrayList<LibroModel> obtenerLibros() {
        return libroService.obtenerLibros();
    }

    @PostMapping
    public LibroModel guardarLibro(@RequestBody LibroModel libro) {
        return libroService.guardarLibro(libro);
    }

    @GetMapping("/{isbn}")
    public Optional<LibroModel> obtenerPorId( @PathVariable("isbn") Long isbn) {
        return libroService.obtenePorId(isbn);
    }

    @PutMapping("/{isbn}")
    public LibroModel actualizarLibro( @RequestBody LibroModel request,@PathVariable("isbn") Long isbn) {
        return libroService.actualizarLibro(request, isbn);
    }

    @DeleteMapping("/{isbn}")
    public String eliminarPorId(@PathVariable("isbn") Long isbn) {

        boolean ok = libroService.eliminarLibro(isbn);

        if (ok) {
            return "Se eliminó el libro ISBN: " + isbn;
        } else {
            return "No se pudo eliminar el libro con ISBN: " + isbn;
        }
    }

    @GetMapping("/autor/{autor}")
    public ArrayList<LibroModel> buscarPorAutor( @PathVariable("autor") String autor) {
        return libroService.buscarPorAutor(autor);
    }

    @GetMapping("/titulo/{titulo}")
    public ArrayList<LibroModel> buscarPorTitulo( @PathVariable("titulo") String titulo) {
        return libroService.buscarPorTitulo(titulo);
    }
}
