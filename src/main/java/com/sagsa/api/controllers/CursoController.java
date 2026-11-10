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

import com.sagsa.api.models.Curso;
import com.sagsa.api.models.dtos.CursoRequestDTO;
import com.sagsa.api.models.dtos.CursoResponseDTO;
import com.sagsa.api.services.CursoService;

@RestController
@RequestMapping("/cursos")
@Validated
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> findAll() {
        List<CursoResponseDTO> list = this.cursoService.findAll().stream().map(CursoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> findById(@PathVariable Long id) {
        Curso obj = this.cursoService.findById(id);
        return ResponseEntity.ok().body(CursoResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /cursos/area/1
    @GetMapping("/area/{areaTecnologicaId}")
    public ResponseEntity<List<CursoResponseDTO>> findByAreaTecnologica(@PathVariable Long areaTecnologicaId) {
        List<CursoResponseDTO> list = this.cursoService.findByAreaTecnologica(areaTecnologicaId).stream().map(CursoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CursoRequestDTO objDto) {
        Curso obj = this.cursoService.fromDTO(objDto);
        Curso newObj = this.cursoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody CursoRequestDTO objDto, @PathVariable Long id) {
        Curso obj = this.cursoService.fromDTO(objDto);
        obj.setId(id);
        this.cursoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
