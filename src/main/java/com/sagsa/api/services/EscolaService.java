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

import com.sagsa.api.models.Escola;
import com.sagsa.api.models.dtos.EscolaRequestDTO;
import com.sagsa.api.repositories.EscolaRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de escolas. Leitura: qualquer usuário autenticado. Escrita: ADMIN.
@Service
public class EscolaService {

    @Autowired
    private EscolaRepository escolaRepository;

    @Transactional(readOnly = true)
    public Escola findById(Long id) {
        Optional<Escola> escola = this.escolaRepository.findById(id);
        Escola obj = escola.orElseThrow(() -> new ObjectNotFoundException(
                "Escola não encontrada! Id: " + id + ", Tipo: " + Escola.class.getName()));
        return obj;
    }

    @Transactional(readOnly = true)
    public List<Escola> findAll() {
        return this.escolaRepository.findAll(Sort.by("nome"));
    }

    @Transactional
    public Escola create(Escola obj) {
        UsuarioService.checkAdmin();
        obj.setId(null);
        if (Objects.isNull(obj.getEstado())) {
            obj.setEstado("RS");
        }
        if (Objects.isNull(obj.getAtivo())) {
            obj.setAtivo(true);
        }
        return this.escolaRepository.save(obj);
    }

    @Transactional
    public Escola update(Escola obj) {
        UsuarioService.checkAdmin();
        Escola newObj = findById(obj.getId());
        newObj.setNome(obj.getNome());
        newObj.setCodigoUnidade(obj.getCodigoUnidade());
        newObj.setCidade(obj.getCidade());
        if (Objects.nonNull(obj.getEstado())) {
            newObj.setEstado(obj.getEstado());
        }
        if (Objects.nonNull(obj.getAtivo())) {
            newObj.setAtivo(obj.getAtivo());
        }
        return this.escolaRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkAdmin();
        findById(id);
        try {
            this.escolaRepository.deleteById(id);
            this.escolaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public Escola fromDTO(@Valid EscolaRequestDTO obj) {
        Escola escola = new Escola();
        escola.setNome(obj.getNome());
        escola.setCodigoUnidade(obj.getCodigoUnidade());
        escola.setCidade(obj.getCidade());
        escola.setEstado(Objects.nonNull(obj.getEstado()) ? obj.getEstado().trim().toUpperCase() : null);
        escola.setAtivo(obj.getAtivo());
        return escola;
    }
}
