package com.vitu.spring.hoverfly.service;

import com.vitu.spring.hoverfly.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final DocumentoService documentoService;
    private final EnderecoService enderecoService;

    public Pessoa getPessoa() {
        return Pessoa.builder().nome("vitu")
                .documento(documentoService.getDocumentoById(2L))
                .endereco(enderecoService.getEnderecoById(2L))
                .build();
    }
}
