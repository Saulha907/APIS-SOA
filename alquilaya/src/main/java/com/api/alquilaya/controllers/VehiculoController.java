package com.api.alquilaya.controllers;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.services.VehiculoService;
@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {
 private final VehiculoService service;
 public VehiculoController(VehiculoService s){service=s;}
 @GetMapping public List<VehiculoModel> all(){return service.listar();}
 @GetMapping("/{id}") public ResponseEntity<VehiculoModel> one(@PathVariable Long id){return service.buscar(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());}
 @PostMapping public ResponseEntity<VehiculoModel> create(@RequestBody VehiculoModel v){return ResponseEntity.status(201).body(service.guardar(v));}
 @PutMapping("/{id}") public ResponseEntity<VehiculoModel> update(@PathVariable Long id,@RequestBody VehiculoModel v){if(service.buscar(id).isEmpty())return ResponseEntity.notFound().build();v.setIdVehiculo(id);return ResponseEntity.ok(service.guardar(v));}
 @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){if(service.buscar(id).isEmpty())return ResponseEntity.notFound().build();service.eliminar(id);return ResponseEntity.noContent().build();}
}
