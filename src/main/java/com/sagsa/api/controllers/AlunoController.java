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

import com.sagsa.api.models.Aluno;
import com.sagsa.api.models.dtos.AlunoRequestDTO;
import com.sagsa.api.models.dtos.AlunoResponseDTO;
import com.sagsa.api.services.AlunoService;

@RestController
@RequestMapping("/alunos")
@Validated
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> findAll() {
        List<AlunoResponseDTO> list = this.alunoService.findAll().stream().map(AlunoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> findById(@PathVariable Long id) {
        Aluno obj = this.alunoService.findById(id);
        return ResponseEntity.ok().body(AlunoResponseDTO.fromEntity(obj));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody AlunoRequestDTO objDto) {
        Aluno obj = this.alunoService.fromDTO(objDto);
        Aluno newObj = this.alunoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody AlunoRequestDTO objDto, @PathVariable Long id) {
        Aluno obj = this.alunoService.fromDTO(objDto);
        obj.setId(id);
        this.alunoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
