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

import com.sagsa.api.models.SapzEstrategiaEnsino;
import com.sagsa.api.models.dtos.SapzEstrategiaEnsinoRequestDTO;
import com.sagsa.api.models.dtos.SapzEstrategiaEnsinoResponseDTO;
import com.sagsa.api.services.SapzEstrategiaEnsinoService;

@RestController
@RequestMapping("/estrategias-ensino")
@Validated
public class SapzEstrategiaEnsinoController {

    @Autowired
    private SapzEstrategiaEnsinoService sapzEstrategiaEnsinoService;

    @GetMapping("/{id}")
    public ResponseEntity<SapzEstrategiaEnsinoResponseDTO> findById(@PathVariable Long id) {
        SapzEstrategiaEnsino obj = this.sapzEstrategiaEnsinoService.findById(id);
        return ResponseEntity.ok().body(SapzEstrategiaEnsinoResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /estrategias-ensino/situacao/1
    @GetMapping("/situacao/{situacaoAprendizagemId}")
    public ResponseEntity<List<SapzEstrategiaEnsinoResponseDTO>> findBySituacaoAprendizagem(@PathVariable Long situacaoAprendizagemId) {
        List<SapzEstrategiaEnsinoResponseDTO> list = this.sapzEstrategiaEnsinoService.findBySituacaoAprendizagem(situacaoAprendizagemId).stream().map(SapzEstrategiaEnsinoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SapzEstrategiaEnsinoRequestDTO objDto) {
        SapzEstrategiaEnsino obj = this.sapzEstrategiaEnsinoService.fromDTO(objDto);
        SapzEstrategiaEnsino newObj = this.sapzEstrategiaEnsinoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody SapzEstrategiaEnsinoRequestDTO objDto, @PathVariable Long id) {
        SapzEstrategiaEnsino obj = this.sapzEstrategiaEnsinoService.fromDTO(objDto);
        obj.setId(id);
        this.sapzEstrategiaEnsinoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.sapzEstrategiaEnsinoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
