package com.vitu.spring.hoverfly.service;

import com.vitu.spring.hoverfly.domain.Endereco;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://endereco-service-api:8083", name = "endereco-service-client")
public interface EnderecoService {

    @GetMapping(value = "/endereco/{id}")
    Endereco getEnderecoById(@PathVariable Long id);

}
