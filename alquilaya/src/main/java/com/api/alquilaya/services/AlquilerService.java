package com.api.alquilaya.services;
import com.api.alquilaya.models.*;
import com.api.alquilaya.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.util.*;
@Service
public class AlquilerService {
 private final IAlquilerRepository repo;
 private final IVehiculoRepository vehiculos;
 private final IClienteRepository clientes;
 public AlquilerService(IAlquilerRepository r,IVehiculoRepository v,IClienteRepository c){repo=r;vehiculos=v;clientes=c;}
 public List<AlquilerModel> listar(){return repo.findAll();}
 public Optional<AlquilerModel> buscar(Long id){return repo.findById(id);}
 @Transactional public AlquilerModel crear(AlquilerModel a){
  if(a.getVehiculo()==null||a.getVehiculo().getIdVehiculo()==null||a.getCliente()==null||a.getCliente().getIdCliente()==null)throw new IllegalArgumentException("Cliente y vehiculo obligatorios");
  if(a.getFechaAlquiler()==null||a.getFechaDevolucion()==null||!a.getFechaDevolucion().isAfter(a.getFechaAlquiler()))throw new IllegalArgumentException("Fechas invalidas");
  VehiculoModel v=vehiculos.findById(a.getVehiculo().getIdVehiculo()).orElseThrow(()->new IllegalArgumentException("Vehiculo no existe"));
  ClienteModel c=clientes.findById(a.getCliente().getIdCliente()).orElseThrow(()->new IllegalArgumentException("Cliente no existe"));
  if(!"DISPONIBLE".equalsIgnoreCase(v.getEstado()))throw new IllegalArgumentException("Vehiculo no disponible");
  if(repo.existsByVehiculo_IdVehiculoAndEstadoInAndFechaAlquilerLessThanAndFechaDevolucionGreaterThan(v.getIdVehiculo(),List.of("PENDIENTE","ACTIVO"),a.getFechaDevolucion(),a.getFechaAlquiler()))throw new IllegalArgumentException("Vehiculo ocupado en esas fechas");
  long dias=ChronoUnit.DAYS.between(a.getFechaAlquiler(),a.getFechaDevolucion());
  a.setVehiculo(v);a.setCliente(c);a.setEstado("PENDIENTE");a.setPrecioTotal(v.getTarifaDiaria().multiply(BigDecimal.valueOf(dias)));
  return repo.save(a);
 }
 @Transactional public AlquilerModel cambiarEstado(Long id,String estado){
  AlquilerModel a=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Alquiler no existe"));
  if(!(("PENDIENTE".equals(a.getEstado())&&("ACTIVO".equals(estado)||"CANCELADO".equals(estado)))||("ACTIVO".equals(a.getEstado())&&"COMPLETADO".equals(estado))))throw new IllegalArgumentException("Transicion no permitida");
  a.setEstado(estado);return repo.save(a);
 }
 public void eliminar(Long id){repo.deleteById(id);}
}
