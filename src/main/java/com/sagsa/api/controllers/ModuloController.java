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

import com.sagsa.api.models.Modulo;
import com.sagsa.api.models.dtos.ModuloRequestDTO;
import com.sagsa.api.models.dtos.ModuloResponseDTO;
import com.sagsa.api.services.ModuloService;

@RestController
@RequestMapping("/modulos")
@Validated
public class ModuloController {

    @Autowired
    private ModuloService moduloService;

    @GetMapping
    public ResponseEntity<List<ModuloResponseDTO>> findAll() {
        List<ModuloResponseDTO> list = this.moduloService.findAll().stream().map(ModuloResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuloResponseDTO> findById(@PathVariable Long id) {
        Modulo obj = this.moduloService.findById(id);
        return ResponseEntity.ok().body(ModuloResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /modulos/plano/1
    @GetMapping("/plano/{planoCursoId}")
    public ResponseEntity<List<ModuloResponseDTO>> findByPlanoCurso(@PathVariable Long planoCursoId) {
        List<ModuloResponseDTO> list = this.moduloService.findByPlanoCurso(planoCursoId).stream().map(ModuloResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ModuloRequestDTO objDto) {
        Modulo obj = this.moduloService.fromDTO(objDto);
        Modulo newObj = this.moduloService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody ModuloRequestDTO objDto, @PathVariable Long id) {
        Modulo obj = this.moduloService.fromDTO(objDto);
        obj.setId(id);
        this.moduloService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.moduloService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
