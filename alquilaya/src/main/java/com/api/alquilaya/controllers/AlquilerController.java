package com.api.alquilaya.controllers;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.api.alquilaya.models.AlquilerModel;
import com.api.alquilaya.services.AlquilerService;
@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {
 private final AlquilerService service;
 public AlquilerController(AlquilerService s){service=s;}
 @GetMapping public List<AlquilerModel> all(){return service.listar();}
 @GetMapping("/{id}") public ResponseEntity<AlquilerModel> one(@PathVariable Long id){return service.buscar(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());}
 @PostMapping public ResponseEntity<AlquilerModel> create(@RequestBody AlquilerModel a){return ResponseEntity.status(201).body(service.crear(a));}
 @PutMapping("/{id}/estado") public ResponseEntity<AlquilerModel> change(@PathVariable Long id,@RequestBody Map<String,String> body){return ResponseEntity.ok(service.cambiarEstado(id,body.get("estado")));}
 @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){if(service.buscar(id).isEmpty())return ResponseEntity.notFound().build();service.eliminar(id);return ResponseEntity.noContent().build();}
}
