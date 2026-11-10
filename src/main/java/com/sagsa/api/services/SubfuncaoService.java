package com.sagsa.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.Subfuncao;
import com.sagsa.api.models.dtos.SubfuncaoRequestDTO;
import com.sagsa.api.repositories.SubfuncaoRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de subfunções. Leitura: qualquer usuário autenticado. Escrita: ADMIN ou COORDENADOR.
@Service
public class SubfuncaoService {

    @Autowired
    private SubfuncaoRepository subfuncaoRepository;

    @Autowired
    private FuncaoService funcaoService;

    @Transactional(readOnly = true)
    public Subfuncao findById(Long id) {
        Optional<Subfuncao> subfuncao = this.subfuncaoRepository.findById(id);
        Subfuncao obj = subfuncao.orElseThrow(() -> new ObjectNotFoundException(
                "Subfunção não encontrada! Id: " + id + ", Tipo: " + Subfuncao.class.getName()));
        return obj;
    }

    @Transactional(readOnly = true)
    public List<Subfuncao> findAll() {
        return this.subfuncaoRepository.findAll(Sort.by("codigoSubfuncao"));
    }

    @Transactional(readOnly = true)
    public List<Subfuncao> findByFuncao(Long funcaoId) {
        return this.subfuncaoRepository.findByFuncao_IdOrderByCodigoSubfuncaoAsc(funcaoId);
    }

    @Transactional
    public Subfuncao create(Subfuncao obj) {
        UsuarioService.checkGestao();
        obj.setId(null);
        return this.subfuncaoRepository.save(obj);
    }

    @Transactional
    public Subfuncao update(Subfuncao obj) {
        UsuarioService.checkGestao();
        Subfuncao newObj = findById(obj.getId());
        newObj.setFuncao(obj.getFuncao());
        newObj.setCodigoSubfuncao(obj.getCodigoSubfuncao());
        newObj.setDescricao(obj.getDescricao());
        return this.subfuncaoRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkGestao();
        findById(id);
        try {
            this.subfuncaoRepository.deleteById(id);
            this.subfuncaoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public Subfuncao fromDTO(@Valid SubfuncaoRequestDTO obj) {
        Subfuncao subfuncao = new Subfuncao();
        subfuncao.setFuncao(this.funcaoService.findById(obj.getFuncaoId()));
        subfuncao.setCodigoSubfuncao(obj.getCodigoSubfuncao());
        subfuncao.setDescricao(obj.getDescricao());
        return subfuncao;
    }
}
