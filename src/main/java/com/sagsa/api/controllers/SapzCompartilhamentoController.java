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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import com.sagsa.api.models.SapzCompartilhamento;
import com.sagsa.api.models.dtos.SapzCompartilhamentoRequestDTO;
import com.sagsa.api.models.dtos.SapzCompartilhamentoResponseDTO;
import com.sagsa.api.services.SapzCompartilhamentoService;

@RestController
@RequestMapping("/compartilhamentos")
@Validated
public class SapzCompartilhamentoController {

    @Autowired
    private SapzCompartilhamentoService sapzCompartilhamentoService;

    @GetMapping("/{id}")
    public ResponseEntity<SapzCompartilhamentoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(SapzCompartilhamentoResponseDTO.fromEntity(this.sapzCompartilhamentoService.findById(id)));
    }

    @GetMapping("/situacao/{situacaoAprendizagemId}")
    public ResponseEntity<List<SapzCompartilhamentoResponseDTO>> findBySituacaoAprendizagem(@PathVariable Long situacaoAprendizagemId) {
        List<SapzCompartilhamentoResponseDTO> list = this.sapzCompartilhamentoService.findBySituacaoAprendizagem(situacaoAprendizagemId)
                .stream().map(SapzCompartilhamentoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SapzCompartilhamentoRequestDTO objDto) {
        SapzCompartilhamento newObj = this.sapzCompartilhamentoService.create(this.sapzCompartilhamentoService.fromDTO(objDto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.sapzCompartilhamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
