package com.sagsa.api.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.Curso;
import com.sagsa.api.models.dtos.CursoRequestDTO;
import com.sagsa.api.repositories.CursoRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de cursos. Leitura: qualquer usuário autenticado. Escrita: ADMIN ou COORDENADOR.
@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private AreaTecnologicaService areaTecnologicaService;

    @Transactional(readOnly = true)
    public Curso findById(Long id) {
        Optional<Curso> curso = this.cursoRepository.findById(id);
        Curso obj = curso.orElseThrow(() -> new ObjectNotFoundException(
                "Curso não encontrado! Id: " + id + ", Tipo: " + Curso.class.getName()));
        return obj;
    }

    @Transactional(readOnly = true)
    public List<Curso> findAll() {
        return this.cursoRepository.findAll(Sort.by("nome"));
    }

    @Transactional(readOnly = true)
    public List<Curso> findByAreaTecnologica(Long areaTecnologicaId) {
        return this.cursoRepository.findByAreaTecnologica_IdOrderByNomeAsc(areaTecnologicaId);
    }

    @Transactional
    public Curso create(Curso obj) {
        UsuarioService.checkGestao();
        obj.setId(null);
        if (Objects.isNull(obj.getAtivo())) {
            obj.setAtivo(true);
        }
        return this.cursoRepository.save(obj);
    }

    @Transactional
    public Curso update(Curso obj) {
        UsuarioService.checkGestao();
        Curso newObj = findById(obj.getId());
        newObj.setAreaTecnologica(obj.getAreaTecnologica());
        newObj.setNome(obj.getNome());
        newObj.setTipoCurso(obj.getTipoCurso());
        if (Objects.nonNull(obj.getAtivo())) {
            newObj.setAtivo(obj.getAtivo());
        }
        return this.cursoRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkGestao();
        findById(id);
        try {
            this.cursoRepository.deleteById(id);
            this.cursoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public Curso fromDTO(@Valid CursoRequestDTO obj) {
        Curso curso = new Curso();
        if (Objects.nonNull(obj.getAreaTecnologicaId())) {
            curso.setAreaTecnologica(this.areaTecnologicaService.findById(obj.getAreaTecnologicaId()));
        }
        curso.setNome(obj.getNome());
        curso.setTipoCurso(obj.getTipoCurso());
        curso.setAtivo(obj.getAtivo());
        return curso;
    }
}
