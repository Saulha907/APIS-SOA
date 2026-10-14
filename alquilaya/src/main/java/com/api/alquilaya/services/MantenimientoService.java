package com.api.alquilaya.services;
import com.api.alquilaya.models.*;
import com.api.alquilaya.repositories.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class MantenimientoService {
 private final IMantenimientoRepository repo;
 private final IVehiculoRepository vehiculos;
 public MantenimientoService(IMantenimientoRepository r,IVehiculoRepository v){repo=r;vehiculos=v;}
 public List<MantenimientoModel> listar(){return repo.findAll();}
 public Optional<MantenimientoModel> buscar(Long id){return repo.findById(id);}
 public MantenimientoModel guardar(MantenimientoModel m){
  if(m.getVehiculo()==null||m.getVehiculo().getIdVehiculo()==null)throw new IllegalArgumentException("El vehiculo es obligatorio");
  m.setVehiculo(vehiculos.findById(m.getVehiculo().getIdVehiculo()).orElseThrow(()->new IllegalArgumentException("El vehiculo no existe")));
  if(m.getCosto()==null||m.getCosto().signum()<0)throw new IllegalArgumentException("El costo no puede ser negativo");
  return repo.save(m);
 }
 public void eliminar(Long id){repo.deleteById(id);}
}
