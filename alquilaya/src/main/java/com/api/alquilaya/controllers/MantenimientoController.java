package com.api.alquilaya.controllers;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.api.alquilaya.models.MantenimientoModel;
import com.api.alquilaya.services.MantenimientoService;
@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {
 private final MantenimientoService service;
 public MantenimientoController(MantenimientoService s){service=s;}
 @GetMapping public List<MantenimientoModel> all(){return service.listar();}
 @GetMapping("/{id}") public ResponseEntity<MantenimientoModel> one(@PathVariable Long id){return service.buscar(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());}
 @PostMapping public ResponseEntity<MantenimientoModel> create(@RequestBody MantenimientoModel v){return ResponseEntity.status(201).body(service.guardar(v));}
 @PutMapping("/{id}") public ResponseEntity<MantenimientoModel> update(@PathVariable Long id,@RequestBody MantenimientoModel v){if(service.buscar(id).isEmpty())return ResponseEntity.notFound().build();v.setIdMantenimiento(id);return ResponseEntity.ok(service.guardar(v));}
 @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){if(service.buscar(id).isEmpty())return ResponseEntity.notFound().build();service.eliminar(id);return ResponseEntity.noContent().build();}
}
