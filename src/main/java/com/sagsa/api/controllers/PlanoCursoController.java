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

import com.sagsa.api.models.PlanoCurso;
import com.sagsa.api.models.dtos.PlanoCursoRequestDTO;
import com.sagsa.api.models.dtos.PlanoCursoResponseDTO;
import com.sagsa.api.services.PlanoCursoService;

@RestController
@RequestMapping("/planos-curso")
@Validated
public class PlanoCursoController {

    @Autowired
    private PlanoCursoService planoCursoService;

    @GetMapping
    public ResponseEntity<List<PlanoCursoResponseDTO>> findAll() {
        List<PlanoCursoResponseDTO> list = this.planoCursoService.findAll().stream().map(PlanoCursoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoCursoResponseDTO> findById(@PathVariable Long id) {
        PlanoCurso obj = this.planoCursoService.findById(id);
        return ResponseEntity.ok().body(PlanoCursoResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /planos-curso/curso/1
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<PlanoCursoResponseDTO>> findByCurso(@PathVariable Long cursoId) {
        List<PlanoCursoResponseDTO> list = this.planoCursoService.findByCurso(cursoId).stream().map(PlanoCursoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody PlanoCursoRequestDTO objDto) {
        PlanoCurso obj = this.planoCursoService.fromDTO(objDto);
        PlanoCurso newObj = this.planoCursoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody PlanoCursoRequestDTO objDto, @PathVariable Long id) {
        PlanoCurso obj = this.planoCursoService.fromDTO(objDto);
        obj.setId(id);
        this.planoCursoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.planoCursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
