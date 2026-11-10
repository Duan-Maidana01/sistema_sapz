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

import com.sagsa.api.models.SapzCriterioAvaliacao;
import com.sagsa.api.models.dtos.SapzCriterioAvaliacaoRequestDTO;
import com.sagsa.api.models.dtos.SapzCriterioAvaliacaoResponseDTO;
import com.sagsa.api.services.SapzCriterioAvaliacaoService;

@RestController
@RequestMapping("/criterios-avaliacao")
@Validated
public class SapzCriterioAvaliacaoController {

    @Autowired
    private SapzCriterioAvaliacaoService sapzCriterioAvaliacaoService;

    @GetMapping("/{id}")
    public ResponseEntity<SapzCriterioAvaliacaoResponseDTO> findById(@PathVariable Long id) {
        SapzCriterioAvaliacao obj = this.sapzCriterioAvaliacaoService.findById(id);
        return ResponseEntity.ok().body(SapzCriterioAvaliacaoResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /criterios-avaliacao/instrumento/1
    @GetMapping("/instrumento/{instrumentoId}")
    public ResponseEntity<List<SapzCriterioAvaliacaoResponseDTO>> findByInstrumento(@PathVariable Long instrumentoId) {
        List<SapzCriterioAvaliacaoResponseDTO> list = this.sapzCriterioAvaliacaoService.findByInstrumento(instrumentoId).stream().map(SapzCriterioAvaliacaoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SapzCriterioAvaliacaoRequestDTO objDto) {
        SapzCriterioAvaliacao obj = this.sapzCriterioAvaliacaoService.fromDTO(objDto);
        SapzCriterioAvaliacao newObj = this.sapzCriterioAvaliacaoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody SapzCriterioAvaliacaoRequestDTO objDto, @PathVariable Long id) {
        SapzCriterioAvaliacao obj = this.sapzCriterioAvaliacaoService.fromDTO(objDto);
        obj.setId(id);
        this.sapzCriterioAvaliacaoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.sapzCriterioAvaliacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
