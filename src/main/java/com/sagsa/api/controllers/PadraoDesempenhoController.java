package com.sagsa.api.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import com.sagsa.api.models.PadraoDesempenho;
import com.sagsa.api.models.dtos.PadraoDesempenhoRequestDTO;
import com.sagsa.api.models.dtos.PadraoDesempenhoResponseDTO;
import com.sagsa.api.services.PadraoDesempenhoService;

@RestController
@RequestMapping("/padroes-desempenho")
@Validated
public class PadraoDesempenhoController {

    @Autowired
    private PadraoDesempenhoService padraoDesempenhoService;

    @GetMapping
    public ResponseEntity<List<PadraoDesempenhoResponseDTO>> findAll() {
        List<PadraoDesempenhoResponseDTO> list = this.padraoDesempenhoService.findAll().stream().map(PadraoDesempenhoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PadraoDesempenhoResponseDTO> findById(@PathVariable Long id) {
        PadraoDesempenho obj = this.padraoDesempenhoService.findById(id);
        return ResponseEntity.ok().body(PadraoDesempenhoResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /padroes-desempenho/subfuncao/1
    @GetMapping("/subfuncao/{subfuncaoId}")
    public ResponseEntity<List<PadraoDesempenhoResponseDTO>> findBySubfuncao(@PathVariable Long subfuncaoId) {
        List<PadraoDesempenhoResponseDTO> list = this.padraoDesempenhoService.findBySubfuncao(subfuncaoId).stream().map(PadraoDesempenhoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody PadraoDesempenhoRequestDTO objDto) {
        PadraoDesempenho obj = this.padraoDesempenhoService.fromDTO(objDto);
        PadraoDesempenho newObj = this.padraoDesempenhoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody PadraoDesempenhoRequestDTO objDto, @PathVariable Long id) {
        PadraoDesempenho obj = this.padraoDesempenhoService.fromDTO(objDto);
        obj.setId(id);
        this.padraoDesempenhoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.padraoDesempenhoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
