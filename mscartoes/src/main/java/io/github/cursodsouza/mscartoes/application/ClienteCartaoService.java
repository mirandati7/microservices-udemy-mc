package io.github.cursodsouza.mscartoes.application;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.cursodsouza.mscartoes.domain.ClienteCartao;
import io.github.cursodsouza.mscartoes.infra.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository repository;

    public List<ClienteCartao> listCartoesByCpf(String cpf){
        return repository.findByCpf(cpf);
    }

    
}
