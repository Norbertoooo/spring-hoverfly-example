package com.vitu.spring.hoverfly.web.rest;

import com.vitu.spring.hoverfly.domain.Pessoa;
import com.vitu.spring.hoverfly.service.PessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pessoa")
@RestController
@RequiredArgsConstructor
public class PessoaResource {

    private final PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<Pessoa> getPessoa() {
        return ResponseEntity.ok().body(pessoaService.getPessoa());
    }

}
