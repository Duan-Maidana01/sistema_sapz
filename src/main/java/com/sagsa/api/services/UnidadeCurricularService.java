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

import com.sagsa.api.models.UnidadeCurricular;
import com.sagsa.api.models.dtos.UnidadeCurricularRequestDTO;
import com.sagsa.api.repositories.UnidadeCurricularRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de unidades curriculares. Leitura: qualquer usuário autenticado. Escrita: ADMIN ou COORDENADOR.
@Service
public class UnidadeCurricularService {

    @Autowired
    private UnidadeCurricularRepository unidadeCurricularRepository;

    @Autowired
    private ModuloService moduloService;

    @Transactional(readOnly = true)
    public UnidadeCurricular findById(Long id) {
        Optional<UnidadeCurricular> unidadeCurricular = this.unidadeCurricularRepository.findById(id);
        UnidadeCurricular obj = unidadeCurricular.orElseThrow(() -> new ObjectNotFoundException(
                "Unidade curricular não encontrada! Id: " + id + ", Tipo: " + UnidadeCurricular.class.getName()));
        return obj;
    }

    @Transactional(readOnly = true)
    public List<UnidadeCurricular> findAll() {
        return this.unidadeCurricularRepository.findAll(Sort.by("ordem"));
    }

    @Transactional(readOnly = true)
    public List<UnidadeCurricular> findByModulo(Long moduloId) {
        return this.unidadeCurricularRepository.findByModulo_IdOrderByOrdemAsc(moduloId);
    }

    @Transactional
    public UnidadeCurricular create(UnidadeCurricular obj) {
        UsuarioService.checkGestao();
        obj.setId(null);
        if (Objects.isNull(obj.getOrdem())) {
            obj.setOrdem(1);
        }
        return this.unidadeCurricularRepository.save(obj);
    }

    @Transactional
    public UnidadeCurricular update(UnidadeCurricular obj) {
        UsuarioService.checkGestao();
        UnidadeCurricular newObj = findById(obj.getId());
        newObj.setModulo(obj.getModulo());
        newObj.setNome(obj.getNome());
        newObj.setCargaHoraria(obj.getCargaHoraria());
        newObj.setObjetivoGeral(obj.getObjetivoGeral());
        if (Objects.nonNull(obj.getOrdem())) {
            newObj.setOrdem(obj.getOrdem());
        }
        return this.unidadeCurricularRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkGestao();
        findById(id);
        try {
            this.unidadeCurricularRepository.deleteById(id);
            this.unidadeCurricularRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public UnidadeCurricular fromDTO(@Valid UnidadeCurricularRequestDTO obj) {
        UnidadeCurricular unidadeCurricular = new UnidadeCurricular();
        unidadeCurricular.setModulo(this.moduloService.findById(obj.getModuloId()));
        unidadeCurricular.setNome(obj.getNome());
        unidadeCurricular.setCargaHoraria(obj.getCargaHoraria());
        unidadeCurricular.setObjetivoGeral(obj.getObjetivoGeral());
        unidadeCurricular.setOrdem(obj.getOrdem());
        return unidadeCurricular;
    }
}
