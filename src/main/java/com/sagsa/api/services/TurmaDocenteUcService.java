package com.sagsa.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.Turma;
import com.sagsa.api.models.TurmaDocenteUc;
import com.sagsa.api.models.TurmaDocenteUcId;
import com.sagsa.api.models.UnidadeCurricular;
import com.sagsa.api.models.Usuario;
import com.sagsa.api.models.dtos.TurmaDocenteUcRequestDTO;
import com.sagsa.api.models.enums.PerfilEnum;
import com.sagsa.api.repositories.TurmaDocenteUcRepository;
import com.sagsa.api.services.exceptions.BusinessRuleException;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio do vínculo instrutor x UC x turma.
// Leitura: qualquer usuário autenticado. Escrita: ADMIN ou coordenador responsável pela turma.
@Service
public class TurmaDocenteUcService {

    @Autowired
    private TurmaDocenteUcRepository turmaDocenteUcRepository;

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private UnidadeCurricularService unidadeCurricularService;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public TurmaDocenteUc findById(Long turmaId, Long unidadeCurricularId, Long instrutorId) {
        return this.turmaDocenteUcRepository.findById(new TurmaDocenteUcId(turmaId, unidadeCurricularId, instrutorId))
                .orElseThrow(() -> new ObjectNotFoundException("Vínculo de docente não encontrado! Turma: " + turmaId
                        + ", UC: " + unidadeCurricularId + ", Instrutor: " + instrutorId));
    }

    @Transactional(readOnly = true)
    public List<TurmaDocenteUc> findByTurma(Long turmaId) {
        this.turmaService.findById(turmaId);
        return this.turmaDocenteUcRepository.findByTurma_Id(turmaId);
    }

    @Transactional(readOnly = true)
    public List<TurmaDocenteUc> findByInstrutor(Long instrutorId) {
        return this.turmaDocenteUcRepository.findByInstrutor_Id(instrutorId);
    }

    // UCs/turmas do instrutor logado
    @Transactional(readOnly = true)
    public List<TurmaDocenteUc> findMinhas() {
        return this.turmaDocenteUcRepository.findByInstrutor_Id(UsuarioService.checkAuthenticated().getId());
    }

    @Transactional
    public TurmaDocenteUc create(TurmaDocenteUc obj) {
        this.turmaService.checkGestaoTurma(obj.getTurma());

        Long planoDaTurma = obj.getTurma().getPlanoCurso().getId();
        Long planoDaUc = obj.getUnidadeCurricular().getModulo().getPlanoCurso().getId();
        if (!planoDaTurma.equals(planoDaUc)) {
            throw new BusinessRuleException("A unidade curricular não pertence ao plano de curso da turma.");
        }

        Usuario instrutor = obj.getInstrutor();
        PerfilEnum perfil = PerfilEnum.fromNome(instrutor.getPerfil().getNome());
        if (!(PerfilEnum.INSTRUTOR.equals(perfil) || PerfilEnum.COORDENADOR.equals(perfil))) {
            throw new BusinessRuleException("O docente deve possuir perfil INSTRUTOR ou COORDENADOR.");
        }
        if (!Boolean.TRUE.equals(instrutor.getAtivo())) {
            throw new BusinessRuleException("O instrutor informado está inativo.");
        }
        if (this.turmaDocenteUcRepository.existsById(obj.getId())) {
            throw new DataBindingViolationException("Este instrutor já está vinculado a esta UC nesta turma.");
        }
        return this.turmaDocenteUcRepository.save(obj);
    }

    @Transactional
    public void delete(Long turmaId, Long unidadeCurricularId, Long instrutorId) {
        TurmaDocenteUc obj = findById(turmaId, unidadeCurricularId, instrutorId);
        this.turmaService.checkGestaoTurma(obj.getTurma());
        this.turmaDocenteUcRepository.delete(obj);
    }

    public TurmaDocenteUc fromDTO(@Valid TurmaDocenteUcRequestDTO obj) {
        Turma turma = this.turmaService.findById(obj.getTurmaId());
        UnidadeCurricular unidadeCurricular = this.unidadeCurricularService.findById(obj.getUnidadeCurricularId());
        Usuario instrutor = this.usuarioService.findByIdSemPermissao(obj.getInstrutorId());
        return new TurmaDocenteUc(turma, unidadeCurricular, instrutor);
    }
}
