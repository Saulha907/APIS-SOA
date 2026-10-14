package com.api.alquilaya.services;
import com.api.alquilaya.models.*;
import com.api.alquilaya.repositories.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class VehiculoService {
 private final IVehiculoRepository repo;
 private final IOficinaAlquilerRepository oficinas;
 public VehiculoService(IVehiculoRepository r,IOficinaAlquilerRepository o){repo=r;oficinas=o;}
 public List<VehiculoModel> listar(){return repo.findAll();}
 public Optional<VehiculoModel> buscar(Long id){return repo.findById(id);}
 public VehiculoModel guardar(VehiculoModel v){
  if(v.getOficina()==null||v.getOficina().getIdOficina()==null)throw new IllegalArgumentException("La oficina es obligatoria");
  v.setOficina(oficinas.findById(v.getOficina().getIdOficina()).orElseThrow(()->new IllegalArgumentException("La oficina no existe")));
  if(v.getTarifaDiaria()==null||v.getTarifaDiaria().signum()<=0)throw new IllegalArgumentException("La tarifa debe ser positiva");
  return repo.save(v);
 }
 public void eliminar(Long id){repo.deleteById(id);}
}
