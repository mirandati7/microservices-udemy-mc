package io.github.cursodsouza.msavaliadorcredito.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import feign.FeignException;
import io.github.cursodsouza.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import io.github.cursodsouza.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicosException;
import io.github.cursodsouza.msavaliadorcredito.application.ex.ErroSolicitacaoCartaoException;
import io.github.cursodsouza.msavaliadorcredito.domain.model.Cartao;
import io.github.cursodsouza.msavaliadorcredito.domain.model.CartaoAprovado;
import io.github.cursodsouza.msavaliadorcredito.domain.model.CartaoCliente;
import io.github.cursodsouza.msavaliadorcredito.domain.model.DadosCliente;
import io.github.cursodsouza.msavaliadorcredito.domain.model.DadosSolicitacaoEmissaoCartao;
import io.github.cursodsouza.msavaliadorcredito.domain.model.ProtocoloSolicitacaoCartao;
import io.github.cursodsouza.msavaliadorcredito.domain.model.RetornoAvaliacaoCliente;
import io.github.cursodsouza.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.cursodsouza.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.cursodsouza.msavaliadorcredito.infra.clients.ClienteResourceClient;
import io.github.cursodsouza.msavaliadorcredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    
    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;

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

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException,
                                                                                    ErroComunicacaoMicroservicosException {    
            try {
               ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
               ResponseEntity<List<Cartao>> cartoesResponse= cartoesClient.getCartoesRendaAte(renda);
               
               List<Cartao> cartoes = cartoesResponse.getBody();
                var  listaCartoesAprovados = cartoes.stream().map(cartao -> {
                  DadosCliente dadosCliente = dadosClienteResponse.getBody();

                  BigDecimal limiteBasico =  cartao.getLimiteBasico();
                  BigDecimal  idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                  var fator = idadeBD.divide(BigDecimal.valueOf(10));
                  BigDecimal limiteAprovado =   fator.multiply(limiteBasico);

                  CartaoAprovado aprovado = new CartaoAprovado();
                  aprovado.setCartao(cartao.getNome());
                  aprovado.setBandeira(cartao.getBandeira());
                  aprovado.setLimiteAprovado(limiteAprovado);
                  return aprovado;
               }).collect(Collectors.toList());
               return new RetornoAvaliacaoCliente(listaCartoesAprovados);
            } catch (FeignException.FeignClientException e) {
               int status = e.status();

               if (HttpStatus.NOT_FOUND.value() == status){
                   throw new DadosClienteNotFoundException();
               }
               throw new ErroComunicacaoMicroservicosException(e.getMessage(), status);
            }  

    }



    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados) {
       try {
         emissaoCartaoPublisher.solicitarCartao(dados);
         var protocolo = UUID.randomUUID().toString();
         return new ProtocoloSolicitacaoCartao(protocolo);
       } catch (Exception e) {
         throw new ErroSolicitacaoCartaoException(e.getMessage());
       }
    }

}
