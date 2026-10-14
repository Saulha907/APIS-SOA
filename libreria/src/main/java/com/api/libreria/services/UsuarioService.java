package com.api.libreria.services;

import com.api.libreria.models.UsuarioModel;
import com.api.libreria.repositories.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    
    public List<UsuarioModel> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    
    public Optional<UsuarioModel> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    
    public UsuarioModel guardarUsuario(UsuarioModel usuario) {
        return usuarioRepository.save(usuario);
    }

    
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}