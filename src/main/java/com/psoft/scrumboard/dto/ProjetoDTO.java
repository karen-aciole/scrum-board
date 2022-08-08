package com.psoft.scrumboard.dto;

import com.psoft.scrumboard.model.Integrante;

public class ProjetoDTO {
    private String nome;

    private String descricao;

    private String instituicaoParceira;

    private String scrumMasterName;


    public ProjetoDTO(String nome, String descricao, String instituicaoParceira, String scrumMasterName) {
        this.nome = nome;
        this.descricao = descricao;
        this.instituicaoParceira = instituicaoParceira;
        this.scrumMasterName = scrumMasterName;
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

    public String getScrumMasterName() {
        return this.scrumMasterName;
    }
}
