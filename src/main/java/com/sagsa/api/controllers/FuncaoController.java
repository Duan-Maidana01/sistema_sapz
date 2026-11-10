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

import com.sagsa.api.models.Funcao;
import com.sagsa.api.models.dtos.FuncaoRequestDTO;
import com.sagsa.api.models.dtos.FuncaoResponseDTO;
import com.sagsa.api.services.FuncaoService;

@RestController
@RequestMapping("/funcoes")
@Validated
public class FuncaoController {

    @Autowired
    private FuncaoService funcaoService;

    @GetMapping
    public ResponseEntity<List<FuncaoResponseDTO>> findAll() {
        List<FuncaoResponseDTO> list = this.funcaoService.findAll().stream().map(FuncaoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncaoResponseDTO> findById(@PathVariable Long id) {
        Funcao obj = this.funcaoService.findById(id);
        return ResponseEntity.ok().body(FuncaoResponseDTO.fromEntity(obj));
    }

    // Ex.: GET /funcoes/plano/1
    @GetMapping("/plano/{planoCursoId}")
    public ResponseEntity<List<FuncaoResponseDTO>> findByPlanoCurso(@PathVariable Long planoCursoId) {
        List<FuncaoResponseDTO> list = this.funcaoService.findByPlanoCurso(planoCursoId).stream().map(FuncaoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody FuncaoRequestDTO objDto) {
        Funcao obj = this.funcaoService.fromDTO(objDto);
        Funcao newObj = this.funcaoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody FuncaoRequestDTO objDto, @PathVariable Long id) {
        Funcao obj = this.funcaoService.fromDTO(objDto);
        obj.setId(id);
        this.funcaoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.funcaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
