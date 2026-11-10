package com.sagsa.api.services;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.exceptions.AuthorizationException;
import com.sagsa.api.models.Capacidade;
import com.sagsa.api.models.Conhecimento;
import com.sagsa.api.models.SapzCriterioAvaliacao;
import com.sagsa.api.models.SapzCriterioRubrica;
import com.sagsa.api.models.SapzEstrategiaEnsino;
import com.sagsa.api.models.SapzInstrumentoAvaliacao;
import com.sagsa.api.models.SapzRecursoAmbiente;
import com.sagsa.api.models.SituacaoAprendizagem;
import com.sagsa.api.models.Turma;
import com.sagsa.api.models.UnidadeCurricular;
import com.sagsa.api.models.dtos.CapacidadeResponseDTO;
import com.sagsa.api.models.dtos.ConhecimentoResponseDTO;
import com.sagsa.api.models.dtos.SapzCriterioAvaliacaoResponseDTO;
import com.sagsa.api.models.dtos.SapzCriterioRubricaResponseDTO;
import com.sagsa.api.models.dtos.SapzEstrategiaEnsinoResponseDTO;
import com.sagsa.api.models.dtos.SapzInstrumentoAvaliacaoResponseDTO;
import com.sagsa.api.models.dtos.SapzRecursoAmbienteResponseDTO;
import com.sagsa.api.models.dtos.SituacaoAprendizagemDetalheDTO;
import com.sagsa.api.models.dtos.SituacaoAprendizagemRequestDTO;
import com.sagsa.api.models.dtos.SituacaoAprendizagemResponseDTO;
import com.sagsa.api.models.enums.PerfilEnum;
import com.sagsa.api.models.projections.SituacaoAprendizagemProjection;
import com.sagsa.api.repositories.CapacidadeRepository;
import com.sagsa.api.repositories.ConhecimentoRepository;
import com.sagsa.api.repositories.SapzCriterioAvaliacaoRepository;
import com.sagsa.api.repositories.SapzCriterioRubricaRepository;
import com.sagsa.api.repositories.SapzEstrategiaEnsinoRepository;
import com.sagsa.api.repositories.SapzInstrumentoAvaliacaoRepository;
import com.sagsa.api.repositories.SapzRecursoAmbienteRepository;
import com.sagsa.api.repositories.SituacaoAprendizagemRepository;
import com.sagsa.api.repositories.TurmaDocenteUcRepository;
import com.sagsa.api.security.UserSpringSecurity;
import com.sagsa.api.services.exceptions.BusinessRuleException;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio das Situações de Aprendizagem (SAPZ).
// Visualização: autor, ADMIN, COORDENADOR ou usuários alcançados por um compartilhamento.
// Alteração/exclusão: somente o autor ou ADMIN.
@Service
public class SituacaoAprendizagemService {

    @Autowired
    private SituacaoAprendizagemRepository situacaoAprendizagemRepository;

    @Autowired
    private CapacidadeRepository capacidadeRepository;

    @Autowired
    private ConhecimentoRepository conhecimentoRepository;

    @Autowired
    private TurmaDocenteUcRepository turmaDocenteUcRepository;

    @Autowired
    private SapzEstrategiaEnsinoRepository sapzEstrategiaEnsinoRepository;

    @Autowired
    private SapzRecursoAmbienteRepository sapzRecursoAmbienteRepository;

    @Autowired
    private SapzInstrumentoAvaliacaoRepository sapzInstrumentoAvaliacaoRepository;

    @Autowired
    private SapzCriterioAvaliacaoRepository sapzCriterioAvaliacaoRepository;

    @Autowired
    private SapzCriterioRubricaRepository sapzCriterioRubricaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UnidadeCurricularService unidadeCurricularService;

    @Autowired
    private TurmaService turmaService;

    // ------------------------------------------------------------------
    // Permissões (usadas também pelos services filhos da SA)
    // ------------------------------------------------------------------

    public void checkCanView(SituacaoAprendizagem sa) {
        UserSpringSecurity userSpringSecurity = UsuarioService.checkAuthenticated();
        if (userSpringSecurity.hasAnyRole(PerfilEnum.ADMIN, PerfilEnum.COORDENADOR) || isAutor(sa, userSpringSecurity)) {
            return;
        }
        if (!this.situacaoAprendizagemRepository.isVisivel(sa.getId(), userSpringSecurity.getId())) {
            throw new AuthorizationException("Acesso negado! Esta situação de aprendizagem não foi compartilhada com você.");
        }
    }

    public void checkOwner(SituacaoAprendizagem sa) {
        UserSpringSecurity userSpringSecurity = UsuarioService.checkAuthenticated();
        if (!userSpringSecurity.hasRole(PerfilEnum.ADMIN) && !isAutor(sa, userSpringSecurity)) {
            throw new AuthorizationException("Acesso negado! Apenas o autor da situação de aprendizagem pode alterá-la.");
        }
    }

    private boolean isAutor(SituacaoAprendizagem sa, UserSpringSecurity userSpringSecurity) {
        return sa.getInstrutor().getId().equals(userSpringSecurity.getId());
    }

    // ------------------------------------------------------------------
    // Consultas
    // ------------------------------------------------------------------

    @Transactional(readOnly = true)
    public SituacaoAprendizagem findById(Long id) {
        Optional<SituacaoAprendizagem> situacaoAprendizagem = this.situacaoAprendizagemRepository.findById(id);
        SituacaoAprendizagem obj = situacaoAprendizagem.orElseThrow(() -> new ObjectNotFoundException(
                "Situação de aprendizagem não encontrada! Id: " + id + ", Tipo: " + SituacaoAprendizagem.class.getName()));
        checkCanView(obj);
        return obj;
    }

    // ADMIN/COORDENADOR: todas. Demais: as próprias + as compartilhadas com o usuário.
    @Transactional(readOnly = true)
    public List<SituacaoAprendizagem> findAll() {
        UserSpringSecurity userSpringSecurity = UsuarioService.checkAuthenticated();
        if (userSpringSecurity.hasAnyRole(PerfilEnum.ADMIN, PerfilEnum.COORDENADOR)) {
            return this.situacaoAprendizagemRepository.findAll(Sort.by(Sort.Direction.DESC, "dataAtualizacao"));
        }
        return this.situacaoAprendizagemRepository.findVisiveis(userSpringSecurity.getId());
    }

    @Transactional(readOnly = true)
    public List<SituacaoAprendizagemProjection> findMinhas() {
        return this.situacaoAprendizagemRepository
                .findByInstrutor_IdOrderByDataAtualizacaoDesc(UsuarioService.checkAuthenticated().getId());
    }

    @Transactional(readOnly = true)
    public List<SituacaoAprendizagem> findCompartilhadasComigo() {
        return this.situacaoAprendizagemRepository.findCompartilhadasComUsuario(UsuarioService.checkAuthenticated().getId());
    }

    @Transactional(readOnly = true)
    public List<SituacaoAprendizagem> findByUnidadeCurricular(Long unidadeCurricularId) {
        this.unidadeCurricularService.findById(unidadeCurricularId);
        UserSpringSecurity userSpringSecurity = UsuarioService.checkAuthenticated();
        if (userSpringSecurity.hasAnyRole(PerfilEnum.ADMIN, PerfilEnum.COORDENADOR)) {
            return this.situacaoAprendizagemRepository.findByUnidadeCurricular_IdOrderByDataAtualizacaoDesc(unidadeCurricularId);
        }
        return this.situacaoAprendizagemRepository.findVisiveis(userSpringSecurity.getId()).stream()
                .filter(sa -> sa.getUnidadeCurricular().getId().equals(unidadeCurricularId))
                .toList();
    }

    // Visão completa da SA (base para o PDF)
    @Transactional(readOnly = true)
    public SituacaoAprendizagemDetalheDTO findDetalhe(Long id) {
        SituacaoAprendizagem sa = findById(id);

        List<SituacaoAprendizagemDetalheDTO.InstrumentoDetalhe> instrumentos =
                this.sapzInstrumentoAvaliacaoRepository.findBySituacaoAprendizagem_IdOrderByIdAsc(id).stream()
                .map(instrumento -> new SituacaoAprendizagemDetalheDTO.InstrumentoDetalhe(
                        SapzInstrumentoAvaliacaoResponseDTO.fromEntity(instrumento),
                        this.sapzCriterioAvaliacaoRepository.findByInstrumento_IdOrderByIdAsc(instrumento.getId()).stream()
                                .map(criterio -> new SituacaoAprendizagemDetalheDTO.CriterioDetalhe(
                                        SapzCriterioAvaliacaoResponseDTO.fromEntity(criterio),
                                        this.sapzCriterioRubricaRepository.findByCriterio_IdOrderByNivelAsc(criterio.getId())
                                                .stream().map(SapzCriterioRubricaResponseDTO::fromEntity).toList()))
                                .toList()))
                .toList();

        return new SituacaoAprendizagemDetalheDTO(
                SituacaoAprendizagemResponseDTO.fromEntity(sa),
                sa.getCapacidades().stream().sorted((a, b) -> a.getId().compareTo(b.getId()))
                        .map(CapacidadeResponseDTO::fromEntity).toList(),
                sa.getConhecimentos().stream().sorted((a, b) -> a.getId().compareTo(b.getId()))
                        .map(ConhecimentoResponseDTO::fromEntity).toList(),
                this.sapzEstrategiaEnsinoRepository.findBySituacaoAprendizagem_IdOrderByIdAsc(id).stream()
                        .map(SapzEstrategiaEnsinoResponseDTO::fromEntity).toList(),
                this.sapzRecursoAmbienteRepository.findBySituacaoAprendizagem_IdOrderByIdAsc(id).stream()
                        .map(SapzRecursoAmbienteResponseDTO::fromEntity).toList(),
                instrumentos);
    }

    // ------------------------------------------------------------------
    // Escrita
    // ------------------------------------------------------------------

    // O autor é sempre o usuário logado
    @Transactional
    public SituacaoAprendizagem create(SituacaoAprendizagem obj) {
        UserSpringSecurity userSpringSecurity = UsuarioService.checkAuthenticated();
        obj.setId(null);
        obj.setInstrutor(this.usuarioService.findByIdSemPermissao(userSpringSecurity.getId()));
        obj.setCompartilhada(false);
        validate(obj, userSpringSecurity);
        return this.situacaoAprendizagemRepository.save(obj);
    }

    @Transactional
    public SituacaoAprendizagem update(SituacaoAprendizagem obj) {
        UserSpringSecurity userSpringSecurity = UsuarioService.checkAuthenticated();
        SituacaoAprendizagem newObj = findById(obj.getId());
        checkOwner(newObj);
        obj.setInstrutor(newObj.getInstrutor());
        validate(obj, userSpringSecurity);

        // Não permite remover capacidades que já são avaliadas em critérios desta SA
        for (Capacidade capacidade : newObj.getCapacidades()) {
            boolean removida = obj.getCapacidades().stream().noneMatch(c -> c.getId().equals(capacidade.getId()));
            if (removida && this.sapzCriterioAvaliacaoRepository
                    .existsByInstrumento_SituacaoAprendizagem_IdAndCapacidade_Id(newObj.getId(), capacidade.getId())) {
                throw new BusinessRuleException("A capacidade " + capacidade.getId()
                        + " é utilizada em critérios de avaliação desta SA e não pode ser removida.");
            }
        }

        newObj.setTitulo(obj.getTitulo());
        newObj.setUnidadeCurricular(obj.getUnidadeCurricular());
        newObj.setTurma(obj.getTurma());
        newObj.setTipoEstrategia(obj.getTipoEstrategia());
        newObj.setContextualizacao(obj.getContextualizacao());
        newObj.setDesafio(obj.getDesafio());
        newObj.setResultadosEsperados(obj.getResultadosEsperados());
        newObj.setCargaHorariaSa(obj.getCargaHorariaSa());
        newObj.getCapacidades().clear();
        newObj.getCapacidades().addAll(obj.getCapacidades());
        newObj.getConhecimentos().clear();
        newObj.getConhecimentos().addAll(obj.getConhecimentos());
        return this.situacaoAprendizagemRepository.save(newObj);
    }

    // Exclui a SA (estratégias, recursos, instrumentos, compartilhamentos e histórico são removidos em cascata no banco)
    public void delete(Long id) {
        SituacaoAprendizagem obj = findById(id);
        checkOwner(obj);
        try {
            this.situacaoAprendizagemRepository.deleteById(id);
            this.situacaoAprendizagemRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    // Marca/desmarca a SA como compartilhada (usado pelo SapzCompartilhamentoService)
    @Transactional
    public void setCompartilhada(SituacaoAprendizagem sa, boolean compartilhada) {
        sa.setCompartilhada(compartilhada);
        this.situacaoAprendizagemRepository.save(sa);
    }

    // Cria uma cópia completa da SA para o usuário logado (reaproveitamento de SAs compartilhadas)
    @Transactional
    public SituacaoAprendizagem duplicar(Long id) {
        SituacaoAprendizagem original = findById(id); // exige permissão de visualização
        UserSpringSecurity userSpringSecurity = UsuarioService.checkAuthenticated();

        SituacaoAprendizagem copia = new SituacaoAprendizagem();
        copia.setTitulo(truncar("Cópia de " + original.getTitulo(), 200));
        copia.setUnidadeCurricular(original.getUnidadeCurricular());
        copia.setTurma(null); // a cópia não fica vinculada à turma do autor original
        copia.setInstrutor(this.usuarioService.findByIdSemPermissao(userSpringSecurity.getId()));
        copia.setTipoEstrategia(original.getTipoEstrategia());
        copia.setContextualizacao(original.getContextualizacao());
        copia.setDesafio(original.getDesafio());
        copia.setResultadosEsperados(original.getResultadosEsperados());
        copia.setCargaHorariaSa(original.getCargaHorariaSa());
        copia.setCompartilhada(false);
        copia.setCapacidades(new HashSet<>(original.getCapacidades()));
        copia.setConhecimentos(new HashSet<>(original.getConhecimentos()));
        copia = this.situacaoAprendizagemRepository.save(copia);

        for (SapzEstrategiaEnsino e : this.sapzEstrategiaEnsinoRepository.findBySituacaoAprendizagem_IdOrderByIdAsc(id)) {
            this.sapzEstrategiaEnsinoRepository.save(new SapzEstrategiaEnsino(null, copia, e.getNomeEstrategia(), e.getDescricaoAtividade()));
        }
        for (SapzRecursoAmbiente r : this.sapzRecursoAmbienteRepository.findBySituacaoAprendizagem_IdOrderByIdAsc(id)) {
            this.sapzRecursoAmbienteRepository.save(new SapzRecursoAmbiente(null, copia, r.getAmbientePedagogico(), r.getRecursosDidaticos()));
        }
        for (SapzInstrumentoAvaliacao i : this.sapzInstrumentoAvaliacaoRepository.findBySituacaoAprendizagem_IdOrderByIdAsc(id)) {
            SapzInstrumentoAvaliacao novoInstrumento = this.sapzInstrumentoAvaliacaoRepository.save(
                    new SapzInstrumentoAvaliacao(null, copia, i.getNomeInstrumento(), i.getDescricao(), i.getTipoMetodo()));
            for (SapzCriterioAvaliacao c : this.sapzCriterioAvaliacaoRepository.findByInstrumento_IdOrderByIdAsc(i.getId())) {
                SapzCriterioAvaliacao novoCriterio = this.sapzCriterioAvaliacaoRepository.save(
                        new SapzCriterioAvaliacao(null, novoInstrumento, c.getCapacidade(), c.getDescricaoCriterio(), c.getPrioridadePeso()));
                for (SapzCriterioRubrica rb : this.sapzCriterioRubricaRepository.findByCriterio_IdOrderByNivelAsc(c.getId())) {
                    this.sapzCriterioRubricaRepository.save(
                            new SapzCriterioRubrica(null, novoCriterio, rb.getNivel(), rb.getDescricaoRubrica(), rb.getPontuacao()));
                }
            }
        }
        return copia;
    }

    // ------------------------------------------------------------------
    // Regras de negócio
    // ------------------------------------------------------------------

    private void validate(SituacaoAprendizagem obj, UserSpringSecurity userSpringSecurity) {
        UnidadeCurricular uc = obj.getUnidadeCurricular();

        if (obj.getCargaHorariaSa() > uc.getCargaHoraria()) {
            throw new BusinessRuleException("A carga horária da SA (" + obj.getCargaHorariaSa()
                    + "h) não pode ultrapassar a carga horária da unidade curricular (" + uc.getCargaHoraria() + "h).");
        }

        Turma turma = obj.getTurma();
        if (Objects.nonNull(turma)) {
            if (!turma.getPlanoCurso().getId().equals(uc.getModulo().getPlanoCurso().getId())) {
                throw new BusinessRuleException("A turma informada não pertence ao plano de curso da unidade curricular.");
            }
            // Instrutores só podem vincular a SA a turmas/UCs em que lecionam
            boolean isGestao = userSpringSecurity.hasAnyRole(PerfilEnum.ADMIN, PerfilEnum.COORDENADOR);
            if (!isGestao && !this.turmaDocenteUcRepository.existsByTurma_IdAndUnidadeCurricular_IdAndInstrutor_Id(
                    turma.getId(), uc.getId(), obj.getInstrutor().getId())) {
                throw new BusinessRuleException("Você não está vinculado como docente desta unidade curricular nesta turma.");
            }
        }

        for (Capacidade capacidade : obj.getCapacidades()) {
            if (!capacidade.getUnidadeCurricular().getId().equals(uc.getId())) {
                throw new BusinessRuleException("A capacidade " + capacidade.getId() + " não pertence à unidade curricular da SA.");
            }
        }
        for (Conhecimento conhecimento : obj.getConhecimentos()) {
            if (!conhecimento.getUnidadeCurricular().getId().equals(uc.getId())) {
                throw new BusinessRuleException("O conhecimento " + conhecimento.getId() + " não pertence à unidade curricular da SA.");
            }
        }
    }

    private String truncar(String texto, int max) {
        return texto.length() <= max ? texto : texto.substring(0, max);
    }

    public SituacaoAprendizagem fromDTO(@Valid SituacaoAprendizagemRequestDTO obj) {
        SituacaoAprendizagem sa = new SituacaoAprendizagem();
        sa.setTitulo(obj.getTitulo());
        sa.setUnidadeCurricular(this.unidadeCurricularService.findById(obj.getUnidadeCurricularId()));
        if (Objects.nonNull(obj.getTurmaId())) {
            sa.setTurma(this.turmaService.findById(obj.getTurmaId()));
        }
        sa.setTipoEstrategia(obj.getTipoEstrategia());
        sa.setContextualizacao(obj.getContextualizacao());
        sa.setDesafio(obj.getDesafio());
        sa.setResultadosEsperados(obj.getResultadosEsperados());
        sa.setCargaHorariaSa(obj.getCargaHorariaSa());

        Set<Long> capacidadeIds = Objects.nonNull(obj.getCapacidadeIds()) ? obj.getCapacidadeIds() : new HashSet<>();
        List<Capacidade> capacidades = this.capacidadeRepository.findAllById(capacidadeIds);
        if (capacidades.size() != capacidadeIds.size()) {
            throw new ObjectNotFoundException("Uma ou mais capacidades informadas não foram encontradas.");
        }
        sa.setCapacidades(new HashSet<>(capacidades));

        Set<Long> conhecimentoIds = Objects.nonNull(obj.getConhecimentoIds()) ? obj.getConhecimentoIds() : new HashSet<>();
        List<Conhecimento> conhecimentos = this.conhecimentoRepository.findAllById(conhecimentoIds);
        if (conhecimentos.size() != conhecimentoIds.size()) {
            throw new ObjectNotFoundException("Um ou mais conhecimentos informados não foram encontrados.");
        }
        sa.setConhecimentos(new HashSet<>(conhecimentos));
        return sa;
    }
}
