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

import com.sagsa.api.models.Perfil;
import com.sagsa.api.models.dtos.PerfilRequestDTO;
import com.sagsa.api.models.dtos.PerfilResponseDTO;
import com.sagsa.api.services.PerfilService;

@RestController
@RequestMapping("/perfis")
@Validated
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @GetMapping
    public ResponseEntity<List<PerfilResponseDTO>> findAll() {
        List<PerfilResponseDTO> list = this.perfilService.findAll().stream().map(PerfilResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(PerfilResponseDTO.fromEntity(this.perfilService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody PerfilRequestDTO objDto) {
        Perfil newObj = this.perfilService.create(this.perfilService.fromDTO(objDto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody PerfilRequestDTO objDto, @PathVariable Long id) {
        Perfil obj = this.perfilService.fromDTO(objDto);
        obj.setId(id);
        this.perfilService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.perfilService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
