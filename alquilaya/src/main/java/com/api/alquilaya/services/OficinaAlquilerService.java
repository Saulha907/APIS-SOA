package com.api.alquilaya.services;
import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.repositories.IOficinaAlquilerRepository;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class OficinaAlquilerService {
 private final IOficinaAlquilerRepository repo;
 public OficinaAlquilerService(IOficinaAlquilerRepository r){repo=r;}
 public List<OficinaAlquilerModel> listar(){return repo.findAll();}
 public Optional<OficinaAlquilerModel> buscar(Long id){return repo.findById(id);}
 public OficinaAlquilerModel guardar(OficinaAlquilerModel v){return repo.save(v);}
 public void eliminar(Long id){repo.deleteById(id);}
}
