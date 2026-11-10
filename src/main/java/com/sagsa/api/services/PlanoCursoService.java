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

import com.sagsa.api.models.PlanoCurso;
import com.sagsa.api.models.dtos.PlanoCursoRequestDTO;
import com.sagsa.api.repositories.PlanoCursoRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de planos de curso. Leitura: qualquer usuário autenticado. Escrita: ADMIN ou COORDENADOR.
@Service
public class PlanoCursoService {

    @Autowired
    private PlanoCursoRepository planoCursoRepository;

    @Autowired
    private CursoService cursoService;

    @Transactional(readOnly = true)
    public PlanoCurso findById(Long id) {
        Optional<PlanoCurso> planoCurso = this.planoCursoRepository.findById(id);
        PlanoCurso obj = planoCurso.orElseThrow(() -> new ObjectNotFoundException(
                "Plano de curso não encontrado! Id: " + id + ", Tipo: " + PlanoCurso.class.getName()));
        return obj;
    }

    @Transactional(readOnly = true)
    public List<PlanoCurso> findAll() {
        return this.planoCursoRepository.findAll(Sort.by("codigoPlano"));
    }

    @Transactional(readOnly = true)
    public List<PlanoCurso> findByCurso(Long cursoId) {
        return this.planoCursoRepository.findByCurso_IdOrderByCodigoPlanoAsc(cursoId);
    }

    @Transactional
    public PlanoCurso create(PlanoCurso obj) {
        UsuarioService.checkGestao();
        obj.setId(null);
        if (Objects.isNull(obj.getVersao())) {
            obj.setVersao("1.0");
        }
        if (Objects.isNull(obj.getAtivo())) {
            obj.setAtivo(true);
        }
        return this.planoCursoRepository.save(obj);
    }

    @Transactional
    public PlanoCurso update(PlanoCurso obj) {
        UsuarioService.checkGestao();
        PlanoCurso newObj = findById(obj.getId());
        newObj.setCurso(obj.getCurso());
        newObj.setCodigoPlano(obj.getCodigoPlano());
        if (Objects.nonNull(obj.getVersao())) {
            newObj.setVersao(obj.getVersao());
        }
        newObj.setCargaHorariaTotal(obj.getCargaHorariaTotal());
        newObj.setDataAprovacao(obj.getDataAprovacao());
        if (Objects.nonNull(obj.getAtivo())) {
            newObj.setAtivo(obj.getAtivo());
        }
        return this.planoCursoRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkGestao();
        findById(id);
        try {
            this.planoCursoRepository.deleteById(id);
            this.planoCursoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public PlanoCurso fromDTO(@Valid PlanoCursoRequestDTO obj) {
        PlanoCurso planoCurso = new PlanoCurso();
        planoCurso.setCurso(this.cursoService.findById(obj.getCursoId()));
        planoCurso.setCodigoPlano(obj.getCodigoPlano());
        planoCurso.setVersao(obj.getVersao());
        planoCurso.setCargaHorariaTotal(obj.getCargaHorariaTotal());
        planoCurso.setDataAprovacao(obj.getDataAprovacao());
        planoCurso.setAtivo(obj.getAtivo());
        return planoCurso;
    }
}
