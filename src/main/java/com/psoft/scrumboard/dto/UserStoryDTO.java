package com.psoft.scrumboard.dto;

import com.psoft.scrumboard.model.estagiodesenvolvimento.EstagioDesenvolvimento;

public class UserStoryDTO {

    private String titulo;
    
    private String descricao;
    
    public UserStoryDTO(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getDescricao() {
        return this.descricao;
    }

}
