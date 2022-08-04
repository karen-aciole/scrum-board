package com.psoft.scrumboard.dto;

import com.psoft.scrumboard.model.estagioDesenvolvimento.EstagioDesenvolvimento;

public class UserStoryDTO {

    private String titulo;
    
    private String descricao;

    private String estagioDesenvolvimento; // remover depois que classes de estagio forem implementadas

    public UserStoryDTO(String titulo, String descricao, String estagioDesenvolvimento) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.estagioDesenvolvimento = estagioDesenvolvimento;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEstagioDesenvolvimento() {
        return estagioDesenvolvimento;
    }

}
