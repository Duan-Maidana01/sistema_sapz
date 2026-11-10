package com.sagsa.api.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import com.sagsa.api.exceptions.AuthorizationException;
import com.sagsa.api.models.SapzHistoricoPdf;
import com.sagsa.api.models.SituacaoAprendizagem;
import com.sagsa.api.models.dtos.SapzHistoricoPdfRequestDTO;
import com.sagsa.api.models.enums.PerfilEnum;
import com.sagsa.api.repositories.SapzHistoricoPdfRepository;
import com.sagsa.api.security.UserSpringSecurity;
import com.sagsa.api.services.exceptions.DataBindingViolationException;
import com.sagsa.api.services.exceptions.ObjectNotFoundException;

// Regras de negócio do histórico de PDFs da SA.
// Leitura: quem pode visualizar a SA. Registro: autor da SA, ADMIN ou COORDENADOR. Exclusão: ADMIN.
// O histórico é imutável (não há atualização).
@Service
public class SapzHistoricoPdfService {

    @Autowired
    private SapzHistoricoPdfRepository sapzHistoricoPdfRepository;

    @Autowired
    private SituacaoAprendizagemService situacaoAprendizagemService;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public SapzHistoricoPdf findById(Long id) {
        Optional<SapzHistoricoPdf> historico = this.sapzHistoricoPdfRepository.findById(id);
        SapzHistoricoPdf obj = historico.orElseThrow(() -> new ObjectNotFoundException(
                "Histórico de PDF não encontrado! Id: " + id + ", Tipo: " + SapzHistoricoPdf.class.getName()));
        this.situacaoAprendizagemService.checkCanView(obj.getSituacaoAprendizagem());
        return obj;
    }

    @Transactional(readOnly = true)
    public List<SapzHistoricoPdf> findBySituacaoAprendizagem(Long situacaoAprendizagemId) {
        this.situacaoAprendizagemService.findById(situacaoAprendizagemId);
        return this.sapzHistoricoPdfRepository.findBySituacaoAprendizagem_IdOrderByDataGeracaoDesc(situacaoAprendizagemId);
    }

    @Transactional
    public SapzHistoricoPdf create(SapzHistoricoPdf obj) {
        UserSpringSecurity userSpringSecurity = UsuarioService.checkAuthenticated();
        SituacaoAprendizagem sa = obj.getSituacaoAprendizagem();
        boolean isAutor = sa.getInstrutor().getId().equals(userSpringSecurity.getId());
        if (!isAutor && !userSpringSecurity.hasAnyRole(PerfilEnum.ADMIN, PerfilEnum.COORDENADOR)) {
            throw new AuthorizationException("Acesso negado! Apenas o autor, coordenadores ou administradores podem registrar PDFs.");
        }

        // Versão automática: 1.0, 2.0, 3.0... conforme a quantidade de PDFs já gerados
        if (Objects.isNull(obj.getVersao()) || obj.getVersao().isBlank()) {
            long total = this.sapzHistoricoPdfRepository.countBySituacaoAprendizagem_Id(sa.getId());
            obj.setVersao((total + 1) + ".0");
        }
        if (this.sapzHistoricoPdfRepository.existsBySituacaoAprendizagem_IdAndVersao(sa.getId(), obj.getVersao())) {
            throw new DataBindingViolationException("Já existe um PDF com a versão " + obj.getVersao() + " para esta SA.");
        }

        obj.setId(null);
        obj.setCriadoPor(this.usuarioService.findByIdSemPermissao(userSpringSecurity.getId()));
        if (Objects.nonNull(obj.getHashConteudo())) {
            obj.setHashConteudo(obj.getHashConteudo().toLowerCase());
        }
        return this.sapzHistoricoPdfRepository.save(obj);
    }

    @Transactional
    public void delete(Long id) {
        UsuarioService.checkAdmin();
        SapzHistoricoPdf obj = findById(id);
        this.sapzHistoricoPdfRepository.delete(obj);
    }

    public SapzHistoricoPdf fromDTO(@Valid SapzHistoricoPdfRequestDTO obj) {
        SapzHistoricoPdf historico = new SapzHistoricoPdf();
        historico.setSituacaoAprendizagem(this.situacaoAprendizagemService.findById(obj.getSituacaoAprendizagemId()));
        historico.setVersao(obj.getVersao());
        historico.setCaminhoArquivoPdf(obj.getCaminhoArquivoPdf());
        historico.setHashConteudo(obj.getHashConteudo());
        return historico;
    }
}
