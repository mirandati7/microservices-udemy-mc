package io.github.cursodsouza.msavaliadorcredito.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "msclientes", path = "/clientes" )
public interface ClienteResourceClient {
    
    @GetMapping(params = "cpf")
    ResponseEntity dadosCliente(@RequestParam("cpf") String cpf);
}
