package com.api.alquilaya.controllers;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.services.ClienteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    private final ClienteService clienteService;

    ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Obtener todos los clientes
    @GetMapping
    public List<ClienteModel> getAllClientes() {
        return clienteService.getAllClientes();
    }
    
    // Obtener un cliente por ID
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteModel> getClienteByEmail(@PathVariable String email) {
        Optional <ClienteModel> cliente = clienteService.getClienteByEmail(email);
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());        
    }
    // Obtener un cliente por licencia
    @GetMapping("/licencia/{licencia}")
    public ResponseEntity<ClienteModel> getClienteByLicencia(@PathVariable String licencia) {
        Optional <ClienteModel> cliente = clienteService.getClienteByLicencia(licencia);
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());        
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteModel> getById(@PathVariable Long id) {
        return clienteService.getClienteById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @org.springframework.web.bind.annotation.PostMapping
    public ResponseEntity<ClienteModel> create(@org.springframework.web.bind.annotation.RequestBody ClienteModel cliente){
        return ResponseEntity.status(201).body(clienteService.saveCliente(cliente));
    }
    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ResponseEntity<ClienteModel> update(@PathVariable Long id,@org.springframework.web.bind.annotation.RequestBody ClienteModel cliente){
        if(clienteService.getClienteById(id).isEmpty())return ResponseEntity.notFound().build();
        cliente.setIdCliente(id);return ResponseEntity.ok(clienteService.saveCliente(cliente));
    }
    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if(clienteService.getClienteById(id).isEmpty())return ResponseEntity.notFound().build();
        clienteService.deleteCliente(id);return ResponseEntity.noContent().build();
    }
}
