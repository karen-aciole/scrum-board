package com.psoft.scrumboard.dto;

import com.psoft.scrumboard.model.Integrante;

public class ProjetoDTO {
    private String nome;

    private String descricao;

    private String instituicaoParceira;


    public ProjetoDTO(String nome, String descricao, String instituicaoParceira) {
        this.nome = nome;
        this.descricao = descricao;
        this.instituicaoParceira = instituicaoParceira;
    }

    public String getNome() {
        return this.nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String getInstituicaoParceira() {
        return this.instituicaoParceira;
    }

}
