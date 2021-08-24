package com.vitu.spring.hoverfly.web.rest;

import com.vitu.spring.hoverfly.domain.Documento;
import com.vitu.spring.hoverfly.domain.Endereco;
import com.vitu.spring.hoverfly.domain.Pessoa;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.vitu.spring.hoverfly.AbstractTest.converterParaJson;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class PessoaResourceMockServerTest {

    @Autowired
    MockMvc mockMvc;

    static ClientAndServer mockServerUm;
    static ClientAndServer mockServerDois;

    @BeforeAll
    public static void startMockServer() {
        mockServerUm = startClientAndServer(8082);
        mockServerDois = startClientAndServer(8083);
    }

    @AfterAll
    public static void stopMockServer() {
        mockServerUm.stop();
        mockServerDois.stop();
    }

    String URL = "/pessoa";

    Documento documento = Documento.builder().numero("123").tipo("cpf").build();

    Endereco endereco = Endereco.builder().rua("rua").numero(123L).build();

    Pessoa pessoa = Pessoa.builder().nome("vitu").documento(documento).endereco(endereco).build();

    @Test
    @DisplayName("Deve retorna uma pessoa com endereço e documento")
    void getPessoa() throws Exception {

        new MockServerClient("localhost", 8082)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/documentos/2")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                                .withBody(converterParaJson(documento))
                );

        new MockServerClient("localhost", 8083)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/enderecos/2")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                                .withBody(converterParaJson(endereco))
                );

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(converterParaJson(pessoa)))
                .andDo(print());

    }

}
