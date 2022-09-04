package io.github.cursodsouza.msavaliadorcredito.application.ex;

import lombok.Getter;

public class ErroComunicacaoMicroservicosException extends Exception {

    @Getter
    private Integer status;

    public  ErroComunicacaoMicroservicosException(String msg, Integer status) {
        super(msg);        
        this.status = status;
    }
    
}
