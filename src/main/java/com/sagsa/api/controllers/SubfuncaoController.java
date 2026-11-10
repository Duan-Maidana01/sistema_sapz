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

import com.sagsa.api.models.Subfuncao;
import com.sagsa.api.models.dtos.SubfuncaoRequestDTO;
import com.sagsa.api.models.dtos.SubfuncaoResponseDTO;
import com.sagsa.api.services.SubfuncaoService;

@RestController
@RequestMapping("/subfuncoes")
@Validated
public class SubfuncaoController {

    @Autowired
    private SubfuncaoService subfuncaoService;

    @GetMapping
    public ResponseEntity<List<SubfuncaoResponseDTO>> findAll() {
        List<SubfuncaoResponseDTO> list = this.subfuncaoService.findAll().stream().map(SubfuncaoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubfuncaoResponseDTO> findById(@PathVariable Long id) {
        Subfuncao obj = this.subfuncaoService.findById(id);
        return ResponseEntity.ok().body(SubfuncaoResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /subfuncoes/funcao/1
    @GetMapping("/funcao/{funcaoId}")
    public ResponseEntity<List<SubfuncaoResponseDTO>> findByFuncao(@PathVariable Long funcaoId) {
        List<SubfuncaoResponseDTO> list = this.subfuncaoService.findByFuncao(funcaoId).stream().map(SubfuncaoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SubfuncaoRequestDTO objDto) {
        Subfuncao obj = this.subfuncaoService.fromDTO(objDto);
        Subfuncao newObj = this.subfuncaoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody SubfuncaoRequestDTO objDto, @PathVariable Long id) {
        Subfuncao obj = this.subfuncaoService.fromDTO(objDto);
        obj.setId(id);
        this.subfuncaoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.subfuncaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
