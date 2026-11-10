package com.sagsa.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.Funcao;
import com.sagsa.api.models.dtos.FuncaoRequestDTO;
import com.sagsa.api.repositories.FuncaoRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de funções do perfil profissional. Leitura: qualquer usuário autenticado. Escrita: ADMIN ou COORDENADOR.
@Service
public class FuncaoService {

    @Autowired
    private FuncaoRepository funcaoRepository;

    @Autowired
    private PlanoCursoService planoCursoService;

    @Transactional(readOnly = true)
    public Funcao findById(Long id) {
        Optional<Funcao> funcao = this.funcaoRepository.findById(id);
        Funcao obj = funcao.orElseThrow(() -> new ObjectNotFoundException(
                "Função não encontrada! Id: " + id + ", Tipo: " + Funcao.class.getName()));
        return obj;
    }

    @Transactional(readOnly = true)
    public List<Funcao> findAll() {
        return this.funcaoRepository.findAll(Sort.by("numero"));
    }

    @Transactional(readOnly = true)
    public List<Funcao> findByPlanoCurso(Long planoCursoId) {
        return this.funcaoRepository.findByPlanoCurso_IdOrderByNumeroAsc(planoCursoId);
    }

    @Transactional
    public Funcao create(Funcao obj) {
        UsuarioService.checkGestao();
        obj.setId(null);
        return this.funcaoRepository.save(obj);
    }

    @Transactional
    public Funcao update(Funcao obj) {
        UsuarioService.checkGestao();
        Funcao newObj = findById(obj.getId());
        newObj.setPlanoCurso(obj.getPlanoCurso());
        newObj.setNumero(obj.getNumero());
        newObj.setDescricao(obj.getDescricao());
        return this.funcaoRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkGestao();
        findById(id);
        try {
            this.funcaoRepository.deleteById(id);
            this.funcaoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public Funcao fromDTO(@Valid FuncaoRequestDTO obj) {
        Funcao funcao = new Funcao();
        funcao.setPlanoCurso(this.planoCursoService.findById(obj.getPlanoCursoId()));
        funcao.setNumero(obj.getNumero());
        funcao.setDescricao(obj.getDescricao());
        return funcao;
    }
}
