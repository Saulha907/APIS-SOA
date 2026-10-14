package com.api.alquilaya.controllers;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.services.OficinaAlquilerService;
@RestController
@RequestMapping("/api/oficinas")
public class OficinaAlquilerController {
 private final OficinaAlquilerService service;
 public OficinaAlquilerController(OficinaAlquilerService s){service=s;}
 @GetMapping public List<OficinaAlquilerModel> all(){return service.listar();}
 @GetMapping("/{id}") public ResponseEntity<OficinaAlquilerModel> one(@PathVariable Long id){return service.buscar(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());}
 @PostMapping public ResponseEntity<OficinaAlquilerModel> create(@RequestBody OficinaAlquilerModel v){return ResponseEntity.status(201).body(service.guardar(v));}
 @PutMapping("/{id}") public ResponseEntity<OficinaAlquilerModel> update(@PathVariable Long id,@RequestBody OficinaAlquilerModel v){if(service.buscar(id).isEmpty())return ResponseEntity.notFound().build();v.setIdOficina(id);return ResponseEntity.ok(service.guardar(v));}
 @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){if(service.buscar(id).isEmpty())return ResponseEntity.notFound().build();service.eliminar(id);return ResponseEntity.noContent().build();}
}
