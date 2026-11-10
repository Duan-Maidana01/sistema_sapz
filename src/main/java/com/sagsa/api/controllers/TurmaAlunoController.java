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

import com.sagsa.api.models.TurmaAluno;
import com.sagsa.api.models.dtos.TurmaAlunoCreateDTO;
import com.sagsa.api.models.dtos.TurmaAlunoResponseDTO;
import com.sagsa.api.models.dtos.TurmaAlunoUpdateDTO;
import com.sagsa.api.services.TurmaAlunoService;

// Matrículas de alunos em turmas (tabela turma_alunos)
@RestController
@RequestMapping("/matriculas")
@Validated
public class TurmaAlunoController {

    @Autowired
    private TurmaAlunoService turmaAlunoService;

    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<TurmaAlunoResponseDTO>> findByTurma(@PathVariable Long turmaId) {
        return ResponseEntity.ok().body(toDTO(this.turmaAlunoService.findByTurma(turmaId)));
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<TurmaAlunoResponseDTO>> findByAluno(@PathVariable Long alunoId) {
        return ResponseEntity.ok().body(toDTO(this.turmaAlunoService.findByAluno(alunoId)));
    }

    @GetMapping("/turma/{turmaId}/aluno/{alunoId}")
    public ResponseEntity<TurmaAlunoResponseDTO> findById(@PathVariable Long turmaId, @PathVariable Long alunoId) {
        return ResponseEntity.ok().body(TurmaAlunoResponseDTO.fromEntity(this.turmaAlunoService.findById(turmaId, alunoId)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody TurmaAlunoCreateDTO objDto) {
        TurmaAluno newObj = this.turmaAlunoService.create(this.turmaAlunoService.fromDTO(objDto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/turma/{turmaId}/aluno/{alunoId}")
                .buildAndExpand(newObj.getTurma().getId(), newObj.getAluno().getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/turma/{turmaId}/aluno/{alunoId}")
    public ResponseEntity<Void> update(@Valid @RequestBody TurmaAlunoUpdateDTO objDto, @PathVariable Long turmaId,
            @PathVariable Long alunoId) {
        this.turmaAlunoService.update(turmaId, alunoId, objDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/turma/{turmaId}/aluno/{alunoId}")
    public ResponseEntity<Void> delete(@PathVariable Long turmaId, @PathVariable Long alunoId) {
        this.turmaAlunoService.delete(turmaId, alunoId);
        return ResponseEntity.noContent().build();
    }

    private List<TurmaAlunoResponseDTO> toDTO(List<TurmaAluno> list) {
        return list.stream().map(TurmaAlunoResponseDTO::fromEntity).toList();
    }
}
