package com.sagsa.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.Aluno;
import com.sagsa.api.models.dtos.AlunoRequestDTO;
import com.sagsa.api.repositories.AlunoRepository;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de alunos. Leitura: qualquer usuário autenticado. Escrita: ADMIN ou COORDENADOR.
@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Transactional(readOnly = true)
    public Aluno findById(Long id) {
        Optional<Aluno> aluno = this.alunoRepository.findById(id);
        Aluno obj = aluno.orElseThrow(() -> new ObjectNotFoundException(
                "Aluno não encontrado! Id: " + id + ", Tipo: " + Aluno.class.getName()));
        return obj;
    }

    @Transactional(readOnly = true)
    public List<Aluno> findAll() {
        return this.alunoRepository.findAll(Sort.by("nome"));
    }

    @Transactional
    public Aluno create(Aluno obj) {
        UsuarioService.checkGestao();
        obj.setId(null);
        return this.alunoRepository.save(obj);
    }

    @Transactional
    public Aluno update(Aluno obj) {
        UsuarioService.checkGestao();
        Aluno newObj = findById(obj.getId());
        newObj.setNome(obj.getNome());
        newObj.setCpf(obj.getCpf());
        newObj.setEmail(obj.getEmail());
        newObj.setMatricula(obj.getMatricula());
        return this.alunoRepository.save(newObj);
    }

    public void delete(Long id) {
        UsuarioService.checkGestao();
        findById(id);
        try {
            this.alunoRepository.deleteById(id);
            this.alunoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    public Aluno fromDTO(@Valid AlunoRequestDTO obj) {
        Aluno aluno = new Aluno();
        aluno.setNome(obj.getNome());
        aluno.setCpf(obj.getCpf());
        aluno.setEmail(obj.getEmail());
        aluno.setMatricula(obj.getMatricula());
        return aluno;
    }
}
