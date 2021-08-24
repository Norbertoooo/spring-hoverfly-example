package com.vitu.spring.hoverfly.service;

import com.vitu.spring.hoverfly.domain.Documento;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://documento-service-api:8082", name = "documento-service-client")
// MockServer @FeignClient(url = "http://localhost:8082", name = "documento-service-client")
public interface DocumentoService {

    @GetMapping(value = "/documentos/{id}")
    Documento getDocumentoById(@PathVariable Long id);

}
