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

import com.sagsa.api.models.EixoTecnologico;
import com.sagsa.api.models.dtos.EixoTecnologicoRequestDTO;
import com.sagsa.api.models.dtos.EixoTecnologicoResponseDTO;
import com.sagsa.api.services.EixoTecnologicoService;

@RestController
@RequestMapping("/eixos-tecnologicos")
@Validated
public class EixoTecnologicoController {

    @Autowired
    private EixoTecnologicoService eixoTecnologicoService;

    @GetMapping
    public ResponseEntity<List<EixoTecnologicoResponseDTO>> findAll() {
        List<EixoTecnologicoResponseDTO> list = this.eixoTecnologicoService.findAll().stream().map(EixoTecnologicoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EixoTecnologicoResponseDTO> findById(@PathVariable Long id) {
        EixoTecnologico obj = this.eixoTecnologicoService.findById(id);
        return ResponseEntity.ok().body(EixoTecnologicoResponseDTO.fromEntity(obj));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody EixoTecnologicoRequestDTO objDto) {
        EixoTecnologico obj = this.eixoTecnologicoService.fromDTO(objDto);
        EixoTecnologico newObj = this.eixoTecnologicoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody EixoTecnologicoRequestDTO objDto, @PathVariable Long id) {
        EixoTecnologico obj = this.eixoTecnologicoService.fromDTO(objDto);
        obj.setId(id);
        this.eixoTecnologicoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.eixoTecnologicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
