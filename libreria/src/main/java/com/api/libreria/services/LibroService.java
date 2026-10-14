package com.api.libreria.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.libreria.models.LibroModel;
import com.api.libreria.repositories.ILibroRepository;

@Service
public class LibroService {
    @Autowired
    ILibroRepository libroRepository;

    //modificador de acceso, tipo de retorno, nombre del método y parámetros
    public ArrayList<LibroModel> obtenerLibros(){
        return (ArrayList<LibroModel>) libroRepository.findAll();
    }

    public LibroModel guardarLibro(LibroModel libro){
        return libroRepository.save(libro);
    }

    public Optional<LibroModel>obtenePorId(Long isbn){
        return libroRepository.findById(isbn);
    }


    public LibroModel actualizarLibro(LibroModel request, Long isbn){
        LibroModel libro = libroRepository.findById(isbn)
                .orElseThrow(()-> new IllegalArgumentException("No existe un libro ISBN: "+ isbn));
        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setAñoPublicacion(request.getAñoPublicacion());
        libro.setNumeroEjemplares(request.getNumeroEjemplares());

        return libroRepository.save(libro);

    }

    public boolean eliminarLibro(Long isnb){
        try{
            libroRepository.deleteById(isnb);
            return true;
        }catch(Exception err){
            return false;
        }
    }

     // metodo para buscar libros por autor 
     public ArrayList<LibroModel>buscarPorAutor(String autor){
        return libroRepository.findByAutor(autor);
    }    

     public ArrayList<LibroModel>buscarPorTitulo(String titulo){
        return libroRepository.findByTitulo(titulo);
    }

}


