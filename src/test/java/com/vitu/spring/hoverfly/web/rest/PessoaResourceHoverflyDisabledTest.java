package com.vitu.spring.hoverfly.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitu.spring.hoverfly.domain.Documento;
import com.vitu.spring.hoverfly.domain.Endereco;
import com.vitu.spring.hoverfly.domain.Pessoa;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit.dsl.SingleQuoteHttpBodyConverter;
import io.specto.hoverfly.junit5.HoverflyExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.vitu.spring.hoverfly.AbstractTest.construirBody;
import static com.vitu.spring.hoverfly.AbstractTest.converterParaJson;
import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.HttpBodyConverter.jsonWithSingleQuotes;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PessoaResourceHoverflyDisabledTest {

    @Autowired
    MockMvc mockMvc;

    String URL = "/pessoa";

    String URL_DOCUMENTO_SERVICE = "http://documento-service-api:8082";

    String URL_ENDERECO_SERVICE = "http://endereco-service-api:8083";

    Documento documento = Documento.builder().numero("123").tipo("cpf").build();

    Endereco endereco = Endereco.builder().rua("rua").numero(123L).build();

    Pessoa pessoa = Pessoa.builder().nome("vitu").documento(documento).endereco(endereco).build();
    
    void simularServicos(Hoverfly hoverfly) throws JsonProcessingException {
        // inicializando o hoverfly
        hoverfly.simulate(dsl(
                service(URL_DOCUMENTO_SERVICE)
                        .get("/documentos/2")
                        .willReturn(success().body(construirBody(documento))),

                service(URL_ENDERECO_SERVICE)
                        .get("/endereco/2")
                        .willReturn(success().body(construirBody(endereco)))
                )
        );

    }

    void simularServicosVazios(Hoverfly hoverfly) {
        hoverfly.simulate(dsl());
    }

    @Test
    @DisplayName("Deve retorna uma pessoa com endereço e documento")
    void getPessoa(Hoverfly hoverfly) throws Exception {

        simularServicos(hoverfly);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(converterParaJson(pessoa)))
                .andDo(print());

    }

    @Test
    void getPessoaWithError() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("error").value("Internal Server Error"))
                .andDo(print());
    }

}
