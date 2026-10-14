package com.api.alquilaya.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.repositories.IClienteRepository;

@Service 
public class ClienteService {

    private final IClienteRepository clienteRepository;


    ClienteService(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteModel> getAllClientes() {
        return clienteRepository.findAll();
    }

    public ClienteModel saveCliente(ClienteModel cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<ClienteModel>  getClienteByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Optional<ClienteModel>  getClienteByLicencia(String licencia) {
        return clienteRepository.findByLicenciaConducir(licencia);
    }

    public Optional<ClienteModel> getClienteById(Long id) { return clienteRepository.findById(id); }

    public void deleteCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}
