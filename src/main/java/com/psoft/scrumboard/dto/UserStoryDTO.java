package com.psoft.scrumboard.dto;

public class UserStoryDTO {

    private String titulo;
    private String descricao;

    public UserStoryDTO(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

}
