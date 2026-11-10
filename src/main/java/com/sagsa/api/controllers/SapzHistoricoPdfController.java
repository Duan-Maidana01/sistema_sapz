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

import com.sagsa.api.models.SapzHistoricoPdf;
import com.sagsa.api.models.dtos.SapzHistoricoPdfRequestDTO;
import com.sagsa.api.models.dtos.SapzHistoricoPdfResponseDTO;
import com.sagsa.api.services.SapzHistoricoPdfService;

@RestController
@RequestMapping("/historico-pdf")
@Validated
public class SapzHistoricoPdfController {

    @Autowired
    private SapzHistoricoPdfService sapzHistoricoPdfService;

    @GetMapping("/{id}")
    public ResponseEntity<SapzHistoricoPdfResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(SapzHistoricoPdfResponseDTO.fromEntity(this.sapzHistoricoPdfService.findById(id)));
    }

    @GetMapping("/situacao/{situacaoAprendizagemId}")
    public ResponseEntity<List<SapzHistoricoPdfResponseDTO>> findBySituacaoAprendizagem(@PathVariable Long situacaoAprendizagemId) {
        List<SapzHistoricoPdfResponseDTO> list = this.sapzHistoricoPdfService.findBySituacaoAprendizagem(situacaoAprendizagemId)
                .stream().map(SapzHistoricoPdfResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SapzHistoricoPdfRequestDTO objDto) {
        SapzHistoricoPdf newObj = this.sapzHistoricoPdfService.create(this.sapzHistoricoPdfService.fromDTO(objDto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.sapzHistoricoPdfService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
