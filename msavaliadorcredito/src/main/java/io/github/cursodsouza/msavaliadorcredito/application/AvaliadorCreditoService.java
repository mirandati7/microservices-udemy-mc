package io.github.cursodsouza.msavaliadorcredito.application;

import java.util.List;

import org.aspectj.util.LangUtil.ProcessController.Thrown;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import feign.FeignException;
import io.github.cursodsouza.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import io.github.cursodsouza.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicosException;
import io.github.cursodsouza.msavaliadorcredito.domain.model.CartaoCliente;
import io.github.cursodsouza.msavaliadorcredito.domain.model.DadosCliente;
import io.github.cursodsouza.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.cursodsouza.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.cursodsouza.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    
    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException,
                                                                   ErroComunicacaoMicroservicosException {
       try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesReponse = cartoesClient.getCartoesByCliente(cpf);
            
            return SituacaoCliente.builder()
                        .cliente(dadosClienteResponse.getBody())
                        .cartoes(cartoesReponse.getBody())
                        .build();
       } catch (FeignException.FeignClientException e) {
          int status = e.status();
          
          if (HttpStatus.NOT_FOUND.value() == status){
            throw new DadosClienteNotFoundException();
          }

          throw new ErroComunicacaoMicroservicosException(e.getMessage(), status);
       }
    }

}
