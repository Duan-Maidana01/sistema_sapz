package com.sagsa.api.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.models.SapzCompartilhamento;
import com.sagsa.api.models.SituacaoAprendizagem;
import com.sagsa.api.models.dtos.SapzCompartilhamentoRequestDTO;
import com.sagsa.api.models.enums.TipoCompartilhamentoEnum;
import com.sagsa.api.repositories.SapzCompartilhamentoRepository;
import com.sagsa.api.services.exceptions.BusinessRuleException;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio de compartilhamento de SAs.
// Leitura: quem pode visualizar a SA. Escrita: autor da SA ou ADMIN.
// Replica a CHECK "chk_compartilhamento_alvo" do banco para devolver mensagens claras (422).
@Service
public class SapzCompartilhamentoService {

    @Autowired
    private SapzCompartilhamentoRepository sapzCompartilhamentoRepository;

    @Autowired
    private SituacaoAprendizagemService situacaoAprendizagemService;

    @Autowired
    private EscolaService escolaService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public SapzCompartilhamento findById(Long id) {
        Optional<SapzCompartilhamento> compartilhamento = this.sapzCompartilhamentoRepository.findById(id);
        SapzCompartilhamento obj = compartilhamento.orElseThrow(() -> new ObjectNotFoundException(
                "Compartilhamento não encontrado! Id: " + id + ", Tipo: " + SapzCompartilhamento.class.getName()));
        this.situacaoAprendizagemService.checkCanView(obj.getSituacaoAprendizagem());
        return obj;
    }

    @Transactional(readOnly = true)
    public List<SapzCompartilhamento> findBySituacaoAprendizagem(Long situacaoAprendizagemId) {
        this.situacaoAprendizagemService.findById(situacaoAprendizagemId);
        return this.sapzCompartilhamentoRepository.findBySituacaoAprendizagem_IdOrderByDataCompartilhamentoDesc(situacaoAprendizagemId);
    }

    @Transactional
    public SapzCompartilhamento create(SapzCompartilhamento obj) {
        SituacaoAprendizagem sa = obj.getSituacaoAprendizagem();
        this.situacaoAprendizagemService.checkOwner(sa);
        validate(obj);

        // Impede compartilhamentos duplicados com o mesmo alvo
        boolean duplicado = this.sapzCompartilhamentoRepository
                .findBySituacaoAprendizagem_IdOrderByDataCompartilhamentoDesc(sa.getId()).stream()
                .anyMatch(c -> c.getTipoCompartilhamento().equals(obj.getTipoCompartilhamento())
                        && Objects.equals(idOf(c.getEscola()), idOf(obj.getEscola()))
                        && Objects.equals(idOf(c.getCurso()), idOf(obj.getCurso()))
                        && Objects.equals(idOf(c.getUsuarioDestinatario()), idOf(obj.getUsuarioDestinatario())));
        if (duplicado) {
            throw new DataBindingViolationException("Esta situação de aprendizagem já está compartilhada com este alvo.");
        }

        obj.setId(null);
        SapzCompartilhamento saved = this.sapzCompartilhamentoRepository.save(obj);
        this.situacaoAprendizagemService.setCompartilhada(sa, true);
        return saved;
    }

    // Remove o compartilhamento; se não restar nenhum, a SA deixa de ser "compartilhada"
    @Transactional
    public void delete(Long id) {
        SapzCompartilhamento obj = findById(id);
        SituacaoAprendizagem sa = obj.getSituacaoAprendizagem();
        this.situacaoAprendizagemService.checkOwner(sa);
        this.sapzCompartilhamentoRepository.delete(obj);
        this.sapzCompartilhamentoRepository.flush();
        if (this.sapzCompartilhamentoRepository.countBySituacaoAprendizagem_Id(sa.getId()) == 0) {
            this.situacaoAprendizagemService.setCompartilhada(sa, false);
        }
    }

    private void validate(SapzCompartilhamento obj) {
        boolean temEscola = Objects.nonNull(obj.getEscola());
        boolean temCurso = Objects.nonNull(obj.getCurso());
        boolean temUsuario = Objects.nonNull(obj.getUsuarioDestinatario());
        TipoCompartilhamentoEnum tipo = obj.getTipoCompartilhamento();

        boolean valido = switch (tipo) {
            case GLOBAL -> !temEscola && !temCurso && !temUsuario;
            case ESCOLA -> temEscola && !temCurso && !temUsuario;
            case CURSO -> temCurso && !temEscola && !temUsuario;
            case USUARIO -> temUsuario && !temEscola && !temCurso;
        };
        if (!valido) {
            throw new BusinessRuleException(switch (tipo) {
                case GLOBAL -> "Compartilhamento GLOBAL não deve informar escola, curso ou usuário.";
                case ESCOLA -> "Compartilhamento por ESCOLA exige apenas o campo escolaId.";
                case CURSO -> "Compartilhamento por CURSO exige apenas o campo cursoId.";
                case USUARIO -> "Compartilhamento por USUARIO exige apenas o campo usuarioDestinatarioId.";
            });
        }
        if (temUsuario) {
            if (obj.getUsuarioDestinatario().getId().equals(obj.getSituacaoAprendizagem().getInstrutor().getId())) {
                throw new BusinessRuleException("Não é possível compartilhar a SA com o próprio autor.");
            }
            if (!Boolean.TRUE.equals(obj.getUsuarioDestinatario().getAtivo())) {
                throw new BusinessRuleException("O usuário destinatário está inativo.");
            }
        }
    }

    private Long idOf(Object entity) {
        if (entity instanceof com.sagsa.api.models.Escola e) return e.getId();
        if (entity instanceof com.sagsa.api.models.Curso c) return c.getId();
        if (entity instanceof com.sagsa.api.models.Usuario u) return u.getId();
        return null;
    }

    public SapzCompartilhamento fromDTO(@Valid SapzCompartilhamentoRequestDTO obj) {
        SapzCompartilhamento compartilhamento = new SapzCompartilhamento();
        compartilhamento.setSituacaoAprendizagem(this.situacaoAprendizagemService.findById(obj.getSituacaoAprendizagemId()));
        compartilhamento.setTipoCompartilhamento(obj.getTipoCompartilhamento());
        if (Objects.nonNull(obj.getEscolaId())) {
            compartilhamento.setEscola(this.escolaService.findById(obj.getEscolaId()));
        }
        if (Objects.nonNull(obj.getCursoId())) {
            compartilhamento.setCurso(this.cursoService.findById(obj.getCursoId()));
        }
        if (Objects.nonNull(obj.getUsuarioDestinatarioId())) {
            compartilhamento.setUsuarioDestinatario(this.usuarioService.findByIdSemPermissao(obj.getUsuarioDestinatarioId()));
        }
        return compartilhamento;
    }
}
