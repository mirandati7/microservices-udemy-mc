package io.github.cursodsouza.msclientes.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.cursodsouza.msclientes.domain.Cliente;
import io.github.cursodsouza.msclientes.infra.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;


    public Cliente save(Cliente cliente) {
        return repository.save(cliente);
    }

    public Optional<Cliente> getByCPF(String cpf) {
        return repository.findByCpf(cpf);
    }
    
}
