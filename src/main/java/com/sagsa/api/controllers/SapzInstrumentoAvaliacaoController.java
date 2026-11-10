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

import com.sagsa.api.models.SapzInstrumentoAvaliacao;
import com.sagsa.api.models.dtos.SapzInstrumentoAvaliacaoRequestDTO;
import com.sagsa.api.models.dtos.SapzInstrumentoAvaliacaoResponseDTO;
import com.sagsa.api.services.SapzInstrumentoAvaliacaoService;

@RestController
@RequestMapping("/instrumentos-avaliacao")
@Validated
public class SapzInstrumentoAvaliacaoController {

    @Autowired
    private SapzInstrumentoAvaliacaoService sapzInstrumentoAvaliacaoService;

    @GetMapping("/{id}")
    public ResponseEntity<SapzInstrumentoAvaliacaoResponseDTO> findById(@PathVariable Long id) {
        SapzInstrumentoAvaliacao obj = this.sapzInstrumentoAvaliacaoService.findById(id);
        return ResponseEntity.ok().body(SapzInstrumentoAvaliacaoResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /instrumentos-avaliacao/situacao/1
    @GetMapping("/situacao/{situacaoAprendizagemId}")
    public ResponseEntity<List<SapzInstrumentoAvaliacaoResponseDTO>> findBySituacaoAprendizagem(@PathVariable Long situacaoAprendizagemId) {
        List<SapzInstrumentoAvaliacaoResponseDTO> list = this.sapzInstrumentoAvaliacaoService.findBySituacaoAprendizagem(situacaoAprendizagemId).stream().map(SapzInstrumentoAvaliacaoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SapzInstrumentoAvaliacaoRequestDTO objDto) {
        SapzInstrumentoAvaliacao obj = this.sapzInstrumentoAvaliacaoService.fromDTO(objDto);
        SapzInstrumentoAvaliacao newObj = this.sapzInstrumentoAvaliacaoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody SapzInstrumentoAvaliacaoRequestDTO objDto, @PathVariable Long id) {
        SapzInstrumentoAvaliacao obj = this.sapzInstrumentoAvaliacaoService.fromDTO(objDto);
        obj.setId(id);
        this.sapzInstrumentoAvaliacaoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.sapzInstrumentoAvaliacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
