package com.sagsa.api.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.TurmaAluno;
import com.sagsa.api.models.TurmaAlunoId;
import com.sagsa.api.models.dtos.TurmaAlunoCreateDTO;
import com.sagsa.api.models.dtos.TurmaAlunoUpdateDTO;
import com.sagsa.api.models.enums.StatusMatriculaEnum;
import com.sagsa.api.repositories.TurmaAlunoRepository;
import com.sagsa.api.services.exceptions.BusinessRuleException;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de matrículas (turma_alunos).
// Leitura: qualquer usuário autenticado. Escrita: ADMIN ou coordenador responsável pela turma.
@Service
public class TurmaAlunoService {

    @Autowired
    private TurmaAlunoRepository turmaAlunoRepository;

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private AlunoService alunoService;

    @Transactional(readOnly = true)
    public TurmaAluno findById(Long turmaId, Long alunoId) {
        return this.turmaAlunoRepository.findById(new TurmaAlunoId(turmaId, alunoId))
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Matrícula não encontrada! Turma: " + turmaId + ", Aluno: " + alunoId));
    }

    @Transactional(readOnly = true)
    public List<TurmaAluno> findByTurma(Long turmaId) {
        this.turmaService.findById(turmaId);
        return this.turmaAlunoRepository.findByTurma_IdOrderByAluno_NomeAsc(turmaId);
    }

    @Transactional(readOnly = true)
    public List<TurmaAluno> findByAluno(Long alunoId) {
        this.alunoService.findById(alunoId);
        return this.turmaAlunoRepository.findByAluno_IdOrderByDataMatriculaDesc(alunoId);
    }

    @Transactional
    public TurmaAluno create(TurmaAluno obj) {
        this.turmaService.checkGestaoTurma(obj.getTurma());
        if (!Boolean.TRUE.equals(obj.getTurma().getAtivo())) {
            throw new BusinessRuleException("Não é possível matricular alunos em uma turma inativa.");
        }
        if (this.turmaAlunoRepository.existsById(obj.getId())) {
            throw new DataBindingViolationException("O aluno já está matriculado nesta turma.");
        }
        if (Objects.isNull(obj.getDataMatricula())) {
            obj.setDataMatricula(LocalDate.now());
        }
        if (Objects.isNull(obj.getStatus())) {
            obj.setStatus(StatusMatriculaEnum.MATRICULADO);
        }
        return this.turmaAlunoRepository.save(obj);
    }

    @Transactional
    public TurmaAluno update(Long turmaId, Long alunoId, @Valid TurmaAlunoUpdateDTO objDto) {
        TurmaAluno newObj = findById(turmaId, alunoId);
        this.turmaService.checkGestaoTurma(newObj.getTurma());
        newObj.setStatus(objDto.getStatus());
        if (Objects.nonNull(objDto.getDataMatricula())) {
            newObj.setDataMatricula(objDto.getDataMatricula());
        }
        return this.turmaAlunoRepository.save(newObj);
    }

    @Transactional
    public void delete(Long turmaId, Long alunoId) {
        TurmaAluno obj = findById(turmaId, alunoId);
        this.turmaService.checkGestaoTurma(obj.getTurma());
        this.turmaAlunoRepository.delete(obj);
    }

    public TurmaAluno fromDTO(@Valid TurmaAlunoCreateDTO obj) {
        TurmaAluno turmaAluno = new TurmaAluno();
        turmaAluno.setTurma(this.turmaService.findById(obj.getTurmaId()));
        turmaAluno.setAluno(this.alunoService.findById(obj.getAlunoId()));
        turmaAluno.setId(new TurmaAlunoId(obj.getTurmaId(), obj.getAlunoId()));
        turmaAluno.setDataMatricula(obj.getDataMatricula());
        turmaAluno.setStatus(obj.getStatus());
        return turmaAluno;
    }
}
