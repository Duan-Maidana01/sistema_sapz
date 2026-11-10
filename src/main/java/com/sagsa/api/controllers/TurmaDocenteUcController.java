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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import com.sagsa.api.models.TurmaDocenteUc;
import com.sagsa.api.models.dtos.TurmaDocenteUcRequestDTO;
import com.sagsa.api.models.dtos.TurmaDocenteUcResponseDTO;
import com.sagsa.api.services.TurmaDocenteUcService;

// Vínculo instrutor x unidade curricular x turma
@RestController
@RequestMapping("/docentes-ucs")
@Validated
public class TurmaDocenteUcController {

    @Autowired
    private TurmaDocenteUcService turmaDocenteUcService;

    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<TurmaDocenteUcResponseDTO>> findByTurma(@PathVariable Long turmaId) {
        return ResponseEntity.ok().body(toDTO(this.turmaDocenteUcService.findByTurma(turmaId)));
    }

    @GetMapping("/instrutor/{instrutorId}")
    public ResponseEntity<List<TurmaDocenteUcResponseDTO>> findByInstrutor(@PathVariable Long instrutorId) {
        return ResponseEntity.ok().body(toDTO(this.turmaDocenteUcService.findByInstrutor(instrutorId)));
    }

    // UCs e turmas do instrutor logado
    @GetMapping("/minhas")
    public ResponseEntity<List<TurmaDocenteUcResponseDTO>> findMinhas() {
        return ResponseEntity.ok().body(toDTO(this.turmaDocenteUcService.findMinhas()));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody TurmaDocenteUcRequestDTO objDto) {
        TurmaDocenteUc newObj = this.turmaDocenteUcService.create(this.turmaDocenteUcService.fromDTO(objDto));
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/docentes-ucs/turma/{turmaId}").buildAndExpand(newObj.getTurma().getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/turma/{turmaId}/uc/{unidadeCurricularId}/instrutor/{instrutorId}")
    public ResponseEntity<Void> delete(@PathVariable Long turmaId, @PathVariable Long unidadeCurricularId,
            @PathVariable Long instrutorId) {
        this.turmaDocenteUcService.delete(turmaId, unidadeCurricularId, instrutorId);
        return ResponseEntity.noContent().build();
    }

    private List<TurmaDocenteUcResponseDTO> toDTO(List<TurmaDocenteUc> list) {
        return list.stream().map(TurmaDocenteUcResponseDTO::fromEntity).toList();
    }
}
