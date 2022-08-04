package com.psoft.scrumboard.dto;

import com.psoft.scrumboard.model.EstagioDesenvolvimento;

public class UserStoryDTO {

    private String titulo;
    
    private String descricao;

    private EstagioDesenvolvimento estagioDesenvolvimento; // remover depois que classes de estagio forem implementadas

    public UserStoryDTO(String titulo, String descricao, EstagioDesenvolvimento estagioDesenvolvimento) {
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

    public EstagioDesenvolvimento getEstagioDesenvolvimento() {
        return estagioDesenvolvimento;
    }

}
