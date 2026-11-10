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

import com.sagsa.api.models.AreaTecnologica;
import com.sagsa.api.models.dtos.AreaTecnologicaRequestDTO;
import com.sagsa.api.models.dtos.AreaTecnologicaResponseDTO;
import com.sagsa.api.services.AreaTecnologicaService;

@RestController
@RequestMapping("/areas-tecnologicas")
@Validated
public class AreaTecnologicaController {

    @Autowired
    private AreaTecnologicaService areaTecnologicaService;

    @GetMapping
    public ResponseEntity<List<AreaTecnologicaResponseDTO>> findAll() {
        List<AreaTecnologicaResponseDTO> list = this.areaTecnologicaService.findAll().stream().map(AreaTecnologicaResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaTecnologicaResponseDTO> findById(@PathVariable Long id) {
        AreaTecnologica obj = this.areaTecnologicaService.findById(id);
        return ResponseEntity.ok().body(AreaTecnologicaResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /areas-tecnologicas/eixo/1
    @GetMapping("/eixo/{eixoTecnologicoId}")
    public ResponseEntity<List<AreaTecnologicaResponseDTO>> findByEixoTecnologico(@PathVariable Long eixoTecnologicoId) {
        List<AreaTecnologicaResponseDTO> list = this.areaTecnologicaService.findByEixoTecnologico(eixoTecnologicoId).stream().map(AreaTecnologicaResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody AreaTecnologicaRequestDTO objDto) {
        AreaTecnologica obj = this.areaTecnologicaService.fromDTO(objDto);
        AreaTecnologica newObj = this.areaTecnologicaService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody AreaTecnologicaRequestDTO objDto, @PathVariable Long id) {
        AreaTecnologica obj = this.areaTecnologicaService.fromDTO(objDto);
        obj.setId(id);
        this.areaTecnologicaService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.areaTecnologicaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
