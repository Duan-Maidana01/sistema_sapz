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

import com.sagsa.api.models.Modulo;
import com.sagsa.api.models.dtos.ModuloRequestDTO;
import com.sagsa.api.repositories.ModuloRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de módulos. Leitura: qualquer usuário autenticado. Escrita: ADMIN ou COORDENADOR.
@Service
public class ModuloService {

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private PlanoCursoService planoCursoService;

    @Transactional(readOnly = true)
    public Modulo findById(Long id) {
        Optional<Modulo> modulo = this.moduloRepository.findById(id);
        Modulo obj = modulo.orElseThrow(() -> new ObjectNotFoundException(
                "Módulo não encontrado! Id: " + id + ", Tipo: " + Modulo.class.getName()));
        return obj;
    }

    @Transactional(readOnly = true)
    public List<Modulo> findAll() {
        return this.moduloRepository.findAll(Sort.by("ordem"));
    }

    @Transactional(readOnly = true)
    public List<Modulo> findByPlanoCurso(Long planoCursoId) {
        return this.moduloRepository.findByPlanoCurso_IdOrderByOrdemAsc(planoCursoId);
    }

    @Transactional
    public Modulo create(Modulo obj) {
        UsuarioService.checkGestao();
        obj.setId(null);
        if (Objects.isNull(obj.getOrdem())) {
            obj.setOrdem(1);
        }
        return this.moduloRepository.save(obj);
    }

    @Transactional
    public Modulo update(Modulo obj) {
        UsuarioService.checkGestao();
        Modulo newObj = findById(obj.getId());
        newObj.setPlanoCurso(obj.getPlanoCurso());
        newObj.setNome(obj.getNome());
        newObj.setTipoModulo(obj.getTipoModulo());
        if (Objects.nonNull(obj.getOrdem())) {
            newObj.setOrdem(obj.getOrdem());
        }
        newObj.setCargaHoraria(obj.getCargaHoraria());
        return this.moduloRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkGestao();
        findById(id);
        try {
            this.moduloRepository.deleteById(id);
            this.moduloRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public Modulo fromDTO(@Valid ModuloRequestDTO obj) {
        Modulo modulo = new Modulo();
        modulo.setPlanoCurso(this.planoCursoService.findById(obj.getPlanoCursoId()));
        modulo.setNome(obj.getNome());
        modulo.setTipoModulo(obj.getTipoModulo());
        modulo.setOrdem(obj.getOrdem());
        modulo.setCargaHoraria(obj.getCargaHoraria());
        return modulo;
    }
}
