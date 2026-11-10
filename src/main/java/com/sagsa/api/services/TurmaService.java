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

import com.sagsa.api.exceptions.AuthorizationException;
import com.sagsa.api.models.Turma;
import com.sagsa.api.models.Usuario;
import com.sagsa.api.models.dtos.TurmaRequestDTO;
import com.sagsa.api.models.enums.PerfilEnum;
import com.sagsa.api.repositories.TurmaRepository;
import com.sagsa.api.security.UserSpringSecurity;
import com.sagsa.api.services.exceptions.BusinessRuleException;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de turmas.
// Leitura: ADMIN/COORDENADOR veem todas; INSTRUTOR vê as turmas em que leciona.
// Escrita: ADMIN, ou COORDENADOR responsável pela turma.
@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private EscolaService escolaService;

    @Autowired
    private PlanoCursoService planoCursoService;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public Turma findById(Long id) {
        Optional<Turma> turma = this.turmaRepository.findById(id);
        return turma.orElseThrow(() -> new ObjectNotFoundException(
                "Turma não encontrada! Id: " + id + ", Tipo: " + Turma.class.getName()));
    }

    @Transactional(readOnly = true)
    public List<Turma> findAll() {
        UserSpringSecurity userSpringSecurity = UsuarioService.checkAuthenticated();
        if (userSpringSecurity.hasAnyRole(PerfilEnum.ADMIN, PerfilEnum.COORDENADOR)) {
            return this.turmaRepository.findAll(Sort.by(Sort.Direction.DESC, "ano", "semestre").and(Sort.by("codigoTurma")));
        }
        return this.turmaRepository.findVinculadasAoUsuario(userSpringSecurity.getId());
    }

    // Turmas em que o usuário logado é coordenador ou instrutor
    @Transactional(readOnly = true)
    public List<Turma> findMinhas() {
        return this.turmaRepository.findVinculadasAoUsuario(UsuarioService.checkAuthenticated().getId());
    }

    @Transactional(readOnly = true)
    public List<Turma> findByEscola(Long escolaId) {
        this.escolaService.findById(escolaId);
        return this.turmaRepository.findByEscola_IdOrderByCodigoTurmaAsc(escolaId);
    }

    @Transactional(readOnly = true)
    public List<Turma> findByPlanoCurso(Long planoCursoId) {
        this.planoCursoService.findById(planoCursoId);
        return this.turmaRepository.findByPlanoCurso_IdOrderByCodigoTurmaAsc(planoCursoId);
    }

    @Transactional
    public Turma create(Turma obj) {
        UserSpringSecurity userSpringSecurity = UsuarioService.checkGestao();
        // Coordenador que cria a turma sem informar o responsável assume a coordenação
        if (Objects.isNull(obj.getCoordenador()) && userSpringSecurity.hasRole(PerfilEnum.COORDENADOR)) {
            obj.setCoordenador(this.usuarioService.findByIdSemPermissao(userSpringSecurity.getId()));
        }
        if (!Boolean.TRUE.equals(obj.getPlanoCurso().getAtivo())) {
            throw new BusinessRuleException("Não é possível criar turma para um plano de curso inativo.");
        }
        validate(obj);
        obj.setId(null);
        if (Objects.isNull(obj.getAtivo())) {
            obj.setAtivo(true);
        }
        return this.turmaRepository.save(obj);
    }

    @Transactional
    public Turma update(Turma obj) {
        Turma newObj = findById(obj.getId());
        checkGestaoTurma(newObj);
        validate(obj);
        newObj.setEscola(obj.getEscola());
        newObj.setPlanoCurso(obj.getPlanoCurso());
        newObj.setCoordenador(obj.getCoordenador());
        newObj.setCodigoTurma(obj.getCodigoTurma());
        newObj.setAno(obj.getAno());
        newObj.setSemestre(obj.getSemestre());
        newObj.setDataInicio(obj.getDataInicio());
        newObj.setDataFim(obj.getDataFim());
        if (Objects.nonNull(obj.getAtivo())) {
            newObj.setAtivo(obj.getAtivo());
        }
        return this.turmaRepository.save(newObj);
    }

    public void delete(Long id) {
        Turma obj = findById(id);
        checkGestaoTurma(obj);
        try {
            this.turmaRepository.deleteById(id);
            this.turmaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas! Desative a turma.");
        }
    }

    // ADMIN sempre pode; COORDENADOR apenas se for o responsável (ou se a turma ainda não tiver coordenador)
    public void checkGestaoTurma(Turma turma) {
        UserSpringSecurity userSpringSecurity = UsuarioService.checkGestao();
        if (userSpringSecurity.hasRole(PerfilEnum.ADMIN)) {
            return;
        }
        Usuario coordenador = turma.getCoordenador();
        if (Objects.nonNull(coordenador) && !coordenador.getId().equals(userSpringSecurity.getId())) {
            throw new AuthorizationException("Acesso negado! Apenas o coordenador responsável pode alterar esta turma.");
        }
    }

    private void validate(Turma obj) {
        if (!obj.getDataFim().isAfter(obj.getDataInicio())) {
            throw new BusinessRuleException("A data de fim deve ser posterior à data de início da turma.");
        }
        boolean duplicado = (obj.getId() == null)
                ? this.turmaRepository.existsByCodigoTurma(obj.getCodigoTurma())
                : this.turmaRepository.existsByCodigoTurmaAndIdNot(obj.getCodigoTurma(), obj.getId());
        if (duplicado) {
            throw new DataBindingViolationException("Já existe uma turma com o código " + obj.getCodigoTurma() + ".");
        }
        Usuario coordenador = obj.getCoordenador();
        if (Objects.nonNull(coordenador)) {
            PerfilEnum perfil = PerfilEnum.fromNome(coordenador.getPerfil().getNome());
            if (!(PerfilEnum.COORDENADOR.equals(perfil) || PerfilEnum.ADMIN.equals(perfil))) {
                throw new BusinessRuleException("O coordenador da turma deve possuir perfil COORDENADOR ou ADMIN.");
            }
            if (!Boolean.TRUE.equals(coordenador.getAtivo())) {
                throw new BusinessRuleException("O coordenador informado está inativo.");
            }
        }
    }

    public Turma fromDTO(@Valid TurmaRequestDTO obj) {
        Turma turma = new Turma();
        turma.setEscola(this.escolaService.findById(obj.getEscolaId()));
        turma.setPlanoCurso(this.planoCursoService.findById(obj.getPlanoCursoId()));
        if (Objects.nonNull(obj.getCoordenadorId())) {
            turma.setCoordenador(this.usuarioService.findByIdSemPermissao(obj.getCoordenadorId()));
        }
        turma.setCodigoTurma(obj.getCodigoTurma().trim().toUpperCase());
        turma.setAno(obj.getAno());
        turma.setSemestre(obj.getSemestre());
        turma.setDataInicio(obj.getDataInicio());
        turma.setDataFim(obj.getDataFim());
        turma.setAtivo(obj.getAtivo());
        return turma;
    }
}
