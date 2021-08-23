package com.vitu.spring.hoverfly;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.specto.hoverfly.junit.dsl.SingleQuoteHttpBodyConverter;

import static io.specto.hoverfly.junit.dsl.HttpBodyConverter.jsonWithSingleQuotes;

public abstract class AbstractTest {

    public static SingleQuoteHttpBodyConverter construirBody(Object objeto) throws JsonProcessingException {
        return jsonWithSingleQuotes(new ObjectMapper().writeValueAsString(objeto));
    }

    public static String converterParaJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
