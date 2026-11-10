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

import com.sagsa.api.models.SapzRecursoAmbiente;
import com.sagsa.api.models.dtos.SapzRecursoAmbienteRequestDTO;
import com.sagsa.api.models.dtos.SapzRecursoAmbienteResponseDTO;
import com.sagsa.api.services.SapzRecursoAmbienteService;

@RestController
@RequestMapping("/recursos-ambientes")
@Validated
public class SapzRecursoAmbienteController {

    @Autowired
    private SapzRecursoAmbienteService sapzRecursoAmbienteService;

    @GetMapping("/{id}")
    public ResponseEntity<SapzRecursoAmbienteResponseDTO> findById(@PathVariable Long id) {
        SapzRecursoAmbiente obj = this.sapzRecursoAmbienteService.findById(id);
        return ResponseEntity.ok().body(SapzRecursoAmbienteResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /recursos-ambientes/situacao/1
    @GetMapping("/situacao/{situacaoAprendizagemId}")
    public ResponseEntity<List<SapzRecursoAmbienteResponseDTO>> findBySituacaoAprendizagem(@PathVariable Long situacaoAprendizagemId) {
        List<SapzRecursoAmbienteResponseDTO> list = this.sapzRecursoAmbienteService.findBySituacaoAprendizagem(situacaoAprendizagemId).stream().map(SapzRecursoAmbienteResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SapzRecursoAmbienteRequestDTO objDto) {
        SapzRecursoAmbiente obj = this.sapzRecursoAmbienteService.fromDTO(objDto);
        SapzRecursoAmbiente newObj = this.sapzRecursoAmbienteService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody SapzRecursoAmbienteRequestDTO objDto, @PathVariable Long id) {
        SapzRecursoAmbiente obj = this.sapzRecursoAmbienteService.fromDTO(objDto);
        obj.setId(id);
        this.sapzRecursoAmbienteService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.sapzRecursoAmbienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
