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

import com.sagsa.api.models.SapzCriterioRubrica;
import com.sagsa.api.models.dtos.SapzCriterioRubricaRequestDTO;
import com.sagsa.api.models.dtos.SapzCriterioRubricaResponseDTO;
import com.sagsa.api.services.SapzCriterioRubricaService;

@RestController
@RequestMapping("/criterio-rubricas")
@Validated
public class SapzCriterioRubricaController {

    @Autowired
    private SapzCriterioRubricaService sapzCriterioRubricaService;

    @GetMapping("/{id}")
    public ResponseEntity<SapzCriterioRubricaResponseDTO> findById(@PathVariable Long id) {
        SapzCriterioRubrica obj = this.sapzCriterioRubricaService.findById(id);
        return ResponseEntity.ok().body(SapzCriterioRubricaResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /criterio-rubricas/criterio/1
    @GetMapping("/criterio/{criterioId}")
    public ResponseEntity<List<SapzCriterioRubricaResponseDTO>> findByCriterio(@PathVariable Long criterioId) {
        List<SapzCriterioRubricaResponseDTO> list = this.sapzCriterioRubricaService.findByCriterio(criterioId).stream().map(SapzCriterioRubricaResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SapzCriterioRubricaRequestDTO objDto) {
        SapzCriterioRubrica obj = this.sapzCriterioRubricaService.fromDTO(objDto);
        SapzCriterioRubrica newObj = this.sapzCriterioRubricaService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody SapzCriterioRubricaRequestDTO objDto, @PathVariable Long id) {
        SapzCriterioRubrica obj = this.sapzCriterioRubricaService.fromDTO(objDto);
        obj.setId(id);
        this.sapzCriterioRubricaService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.sapzCriterioRubricaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
