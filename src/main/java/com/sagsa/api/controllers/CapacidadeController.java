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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import com.sagsa.api.models.Capacidade;
import com.sagsa.api.models.dtos.CapacidadeRequestDTO;
import com.sagsa.api.models.dtos.CapacidadeResponseDTO;
import com.sagsa.api.models.enums.TipoCapacidadeEnum;
import com.sagsa.api.services.CapacidadeService;

@RestController
@RequestMapping("/capacidades")
@Validated
public class CapacidadeController {

    @Autowired
    private CapacidadeService capacidadeService;

    @GetMapping
    public ResponseEntity<List<CapacidadeResponseDTO>> findAll() {
        List<CapacidadeResponseDTO> list = this.capacidadeService.findAll().stream().map(CapacidadeResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CapacidadeResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(CapacidadeResponseDTO.fromEntity(this.capacidadeService.findById(id)));
    }

    // Ex.: GET /capacidades/uc/1?tipo=TECNICA
    @GetMapping("/uc/{unidadeCurricularId}")
    public ResponseEntity<List<CapacidadeResponseDTO>> findByUnidadeCurricular(
            @PathVariable Long unidadeCurricularId,
            @RequestParam(required = false) TipoCapacidadeEnum tipo) {
        List<CapacidadeResponseDTO> list = this.capacidadeService.findByUnidadeCurricular(unidadeCurricularId, tipo)
                .stream().map(CapacidadeResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CapacidadeRequestDTO objDto) {
        Capacidade newObj = this.capacidadeService.create(this.capacidadeService.fromDTO(objDto));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody CapacidadeRequestDTO objDto, @PathVariable Long id) {
        Capacidade obj = this.capacidadeService.fromDTO(objDto);
        obj.setId(id);
        this.capacidadeService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.capacidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
