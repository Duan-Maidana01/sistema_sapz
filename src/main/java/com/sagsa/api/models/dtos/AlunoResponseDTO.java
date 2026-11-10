package com.sagsa.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sagsa.api.models.Aluno;

// Saída (GET) de alunos
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlunoResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String matricula;

    public static AlunoResponseDTO fromEntity(Aluno obj) {
        return new AlunoResponseDTO(
                obj.getId(),
                obj.getNome(),
                obj.getCpf(),
                obj.getEmail(),
                obj.getMatricula());
    }
}
