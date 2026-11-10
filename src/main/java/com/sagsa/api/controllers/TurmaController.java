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

import com.sagsa.api.models.Turma;
import com.sagsa.api.models.dtos.TurmaRequestDTO;
import com.sagsa.api.models.dtos.TurmaResponseDTO;
import com.sagsa.api.services.TurmaService;

@RestController
@RequestMapping("/turmas")
@Validated
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @GetMapping
    public ResponseEntity<List<TurmaResponseDTO>> findAll() {
        return ResponseEntity.ok().body(toDTO(this.turmaService.findAll()));
    }

    // Turmas em que o usuário logado é coordenador ou instrutor
    @GetMapping("/minhas")
    public ResponseEntity<List<TurmaResponseDTO>> findMinhas() {
        return ResponseEntity.ok().body(toDTO(this.turmaService.findMinhas()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(TurmaResponseDTO.fromEntity(this.turmaService.findById(id)));
    }

    @GetMapping("/escola/{escolaId}")
    public ResponseEntity<List<TurmaResponseDTO>> findByEscola(@PathVariable Long escolaId) {
        return ResponseEntity.ok().body(toDTO(this.turmaService.findByEscola(escolaId)));
    }

    @GetMapping("/plano/{planoCursoId}")
    public ResponseEntity<List<TurmaResponseDTO>> findByPlanoCurso(@PathVariable Long planoCursoId) {
        return ResponseEntity.ok().body(toDTO(this.turmaService.findByPlanoCurso(planoCursoId)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody TurmaRequestDTO objDto) {
        Turma newObj = this.turmaService.create(this.turmaService.fromDTO(objDto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody TurmaRequestDTO objDto, @PathVariable Long id) {
        Turma obj = this.turmaService.fromDTO(objDto);
        obj.setId(id);
        this.turmaService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.turmaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private List<TurmaResponseDTO> toDTO(List<Turma> list) {
        return list.stream().map(TurmaResponseDTO::fromEntity).toList();
    }
}
