package io.github.cursodsouza.msavaliadorcredito.infra.clients;

import static io.github.cursodsouza.msavaliadorcredito.infra.clients.CartoesMocks.setupMockBooksResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.tomakehurst.wiremock.WireMockServer;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WireMockConfig.class })
class CartoesClientIntegrationTest {

    @Autowired
    private WireMockServer mockBooksService;

    @Autowired
    private CartoesResourceClient cartoesResourceClient;

    @BeforeEach
    void setUp() throws IOException {
        setupMockBooksResponse(mockBooksService);
    }

    @Test
    public void whenGetBooks_thenBooksShouldBeReturned() {
        Assertions.assertFalse(cartoesResourceClient.getCartoesByCliente("12345678900").getBody().isEmpty());
    }

    @Test
    public void whenGetBooks_thenTheCorrectBooksShouldBeReturned() {
        assertEquals(cartoesResourceClient.getCartoesByCliente("12345678900").getBody().size(), 2); 
          
    }

}
