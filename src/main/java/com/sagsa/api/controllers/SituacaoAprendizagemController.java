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

import com.sagsa.api.models.SituacaoAprendizagem;
import com.sagsa.api.models.dtos.SituacaoAprendizagemDetalheDTO;
import com.sagsa.api.models.dtos.SituacaoAprendizagemRequestDTO;
import com.sagsa.api.models.dtos.SituacaoAprendizagemResponseDTO;
import com.sagsa.api.models.projections.SituacaoAprendizagemProjection;
import com.sagsa.api.services.SituacaoAprendizagemService;

@RestController
@RequestMapping("/situacoes-aprendizagem")
@Validated
public class SituacaoAprendizagemController {

    @Autowired
    private SituacaoAprendizagemService situacaoAprendizagemService;

    @GetMapping
    public ResponseEntity<List<SituacaoAprendizagemResponseDTO>> findAll() {
        return ResponseEntity.ok().body(toDTO(this.situacaoAprendizagemService.findAll()));
    }

    // SAs de autoria do usuário logado (resumo)
    @GetMapping("/minhas")
    public ResponseEntity<List<SituacaoAprendizagemProjection>> findMinhas() {
        return ResponseEntity.ok().body(this.situacaoAprendizagemService.findMinhas());
    }

    // SAs de outros autores compartilhadas com o usuário logado
    @GetMapping("/compartilhadas-comigo")
    public ResponseEntity<List<SituacaoAprendizagemResponseDTO>> findCompartilhadasComigo() {
        return ResponseEntity.ok().body(toDTO(this.situacaoAprendizagemService.findCompartilhadasComigo()));
    }

    @GetMapping("/uc/{unidadeCurricularId}")
    public ResponseEntity<List<SituacaoAprendizagemResponseDTO>> findByUnidadeCurricular(@PathVariable Long unidadeCurricularId) {
        return ResponseEntity.ok().body(toDTO(this.situacaoAprendizagemService.findByUnidadeCurricular(unidadeCurricularId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SituacaoAprendizagemResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(SituacaoAprendizagemResponseDTO.fromEntity(this.situacaoAprendizagemService.findById(id)));
    }

    // Visão completa (capacidades, conhecimentos, estratégias, recursos, instrumentos, critérios e rubricas)
    @GetMapping("/{id}/detalhe")
    public ResponseEntity<SituacaoAprendizagemDetalheDTO> findDetalhe(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.situacaoAprendizagemService.findDetalhe(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SituacaoAprendizagemRequestDTO objDto) {
        SituacaoAprendizagem newObj = this.situacaoAprendizagemService.create(this.situacaoAprendizagemService.fromDTO(objDto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    // Cria uma cópia da SA para o usuário logado
    @PostMapping("/{id}/duplicar")
    public ResponseEntity<Void> duplicar(@PathVariable Long id) {
        SituacaoAprendizagem copia = this.situacaoAprendizagemService.duplicar(id);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/situacoes-aprendizagem/{id}").buildAndExpand(copia.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody SituacaoAprendizagemRequestDTO objDto, @PathVariable Long id) {
        SituacaoAprendizagem obj = this.situacaoAprendizagemService.fromDTO(objDto);
        obj.setId(id);
        this.situacaoAprendizagemService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.situacaoAprendizagemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private List<SituacaoAprendizagemResponseDTO> toDTO(List<SituacaoAprendizagem> list) {
        return list.stream().map(SituacaoAprendizagemResponseDTO::fromEntity).toList();
    }
}
