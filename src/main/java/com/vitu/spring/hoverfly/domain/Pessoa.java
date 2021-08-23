package com.vitu.spring.hoverfly.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pessoa {

    private String nome;
    private Documento documento;
    private Endereco endereco;

}
