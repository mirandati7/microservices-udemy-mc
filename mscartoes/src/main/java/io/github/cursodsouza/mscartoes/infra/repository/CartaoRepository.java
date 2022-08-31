package io.github.cursodsouza.mscartoes.infra.repository;

import io.github.cursodsouza.mscartoes.domain.Cartao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    List<Cartao> findByRendaLessThanEqual(BigDecimal rendaBigDecimal);
    
}
