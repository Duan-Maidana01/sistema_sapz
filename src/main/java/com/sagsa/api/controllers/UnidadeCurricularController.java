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

import com.sagsa.api.models.UnidadeCurricular;
import com.sagsa.api.models.dtos.UnidadeCurricularRequestDTO;
import com.sagsa.api.models.dtos.UnidadeCurricularResponseDTO;
import com.sagsa.api.services.UnidadeCurricularService;

@RestController
@RequestMapping("/unidades-curriculares")
@Validated
public class UnidadeCurricularController {

    @Autowired
    private UnidadeCurricularService unidadeCurricularService;

    @GetMapping
    public ResponseEntity<List<UnidadeCurricularResponseDTO>> findAll() {
        List<UnidadeCurricularResponseDTO> list = this.unidadeCurricularService.findAll().stream().map(UnidadeCurricularResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadeCurricularResponseDTO> findById(@PathVariable Long id) {
        UnidadeCurricular obj = this.unidadeCurricularService.findById(id);
        return ResponseEntity.ok().body(UnidadeCurricularResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /unidades-curriculares/modulo/1
    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<List<UnidadeCurricularResponseDTO>> findByModulo(@PathVariable Long moduloId) {
        List<UnidadeCurricularResponseDTO> list = this.unidadeCurricularService.findByModulo(moduloId).stream().map(UnidadeCurricularResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UnidadeCurricularRequestDTO objDto) {
        UnidadeCurricular obj = this.unidadeCurricularService.fromDTO(objDto);
        UnidadeCurricular newObj = this.unidadeCurricularService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody UnidadeCurricularRequestDTO objDto, @PathVariable Long id) {
        UnidadeCurricular obj = this.unidadeCurricularService.fromDTO(objDto);
        obj.setId(id);
        this.unidadeCurricularService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.unidadeCurricularService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
