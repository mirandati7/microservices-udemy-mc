package io.github.cursodsouza.mscartoes.application;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import io.github.cursodsouza.mscartoes.domain.Cartao;
import io.github.cursodsouza.mscartoes.infra.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartaoService {
    
    private final CartaoRepository repository;

    @Transactional
    public Cartao save(Cartao cartao){
        return repository.save(cartao);
    }

    public List<Cartao> getCartoesRendaMenorIgual(Long renda){
        var rendaBigDecimal = BigDecimal.valueOf(renda);
        return repository.findByRendaLessThanEqual(rendaBigDecimal);
        
    }

}
