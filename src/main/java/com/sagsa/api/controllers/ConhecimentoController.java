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

import com.sagsa.api.models.Conhecimento;
import com.sagsa.api.models.dtos.ConhecimentoRequestDTO;
import com.sagsa.api.models.dtos.ConhecimentoResponseDTO;
import com.sagsa.api.services.ConhecimentoService;

@RestController
@RequestMapping("/conhecimentos")
@Validated
public class ConhecimentoController {

    @Autowired
    private ConhecimentoService conhecimentoService;

    @GetMapping
    public ResponseEntity<List<ConhecimentoResponseDTO>> findAll() {
        List<ConhecimentoResponseDTO> list = this.conhecimentoService.findAll().stream().map(ConhecimentoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConhecimentoResponseDTO> findById(@PathVariable Long id) {
        Conhecimento obj = this.conhecimentoService.findById(id);
        return ResponseEntity.ok().body(ConhecimentoResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /conhecimentos/uc/1
    @GetMapping("/uc/{unidadeCurricularId}")
    public ResponseEntity<List<ConhecimentoResponseDTO>> findByUnidadeCurricular(@PathVariable Long unidadeCurricularId) {
        List<ConhecimentoResponseDTO> list = this.conhecimentoService.findByUnidadeCurricular(unidadeCurricularId).stream().map(ConhecimentoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ConhecimentoRequestDTO objDto) {
        Conhecimento obj = this.conhecimentoService.fromDTO(objDto);
        Conhecimento newObj = this.conhecimentoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody ConhecimentoRequestDTO objDto, @PathVariable Long id) {
        Conhecimento obj = this.conhecimentoService.fromDTO(objDto);
        obj.setId(id);
        this.conhecimentoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.conhecimentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
