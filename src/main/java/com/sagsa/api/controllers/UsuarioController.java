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

import com.sagsa.api.models.Usuario;
import com.sagsa.api.models.dtos.UsuarioCreateDTO;
import com.sagsa.api.models.dtos.UsuarioResponseDTO;
import com.sagsa.api.models.dtos.UsuarioUpdateDTO;
import com.sagsa.api.models.projections.UsuarioProjection;
import com.sagsa.api.services.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@Validated
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // ADMIN / COORDENADOR
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
        List<UsuarioResponseDTO> list = this.usuarioService.findAll().stream().map(UsuarioResponseDTO::fromEntity).toList();
        return ResponseEntity.ok().body(list);
    }

    // Dados do usuário logado
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> findLogged() {
        return ResponseEntity.ok().body(UsuarioResponseDTO.fromEntity(this.usuarioService.findLogged()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(UsuarioResponseDTO.fromEntity(this.usuarioService.findById(id)));
    }

    // Ex.: GET /usuarios/perfil/INSTRUTOR
    @GetMapping("/perfil/{nomePerfil}")
    public ResponseEntity<List<UsuarioProjection>> findByPerfil(@PathVariable String nomePerfil) {
        return ResponseEntity.ok().body(this.usuarioService.findByPerfil(nomePerfil));
    }

    // ADMIN
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UsuarioCreateDTO objDto) {
        Usuario obj = this.usuarioService.fromDTO(objDto);
        Usuario newObj = this.usuarioService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody UsuarioUpdateDTO objDto, @PathVariable Long id) {
        objDto.setId(id);
        Usuario obj = this.usuarioService.fromDTO(objDto);
        this.usuarioService.update(obj);
        return ResponseEntity.noContent().build();
    }

    // ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
