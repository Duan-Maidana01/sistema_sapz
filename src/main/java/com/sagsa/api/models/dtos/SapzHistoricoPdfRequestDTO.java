package com.sagsa.api.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entrada (POST) de registro de um PDF gerado para a SA
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SapzHistoricoPdfRequestDTO {

    @NotNull
    private Long situacaoAprendizagemId;

    // Opcional: se omitida, é gerada automaticamente (1.0, 2.0, 3.0...)
    @Size(max = 20)
    @Pattern(regexp = "\\d+(\\.\\d+)*", message = "deve estar no formato 1.0, 1.1, 2.0...")
    private String versao;

    @NotBlank
    @Size(max = 255)
    private String caminhoArquivoPdf;

    // Opcional: SHA-256 em hexadecimal
    @Pattern(regexp = "[a-fA-F0-9]{64}", message = "deve ser um hash SHA-256 (64 caracteres hexadecimais)")
    private String hashConteudo;
}
