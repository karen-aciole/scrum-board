package com.psoft.scrumboard.dto;

public class TaskDTO {

    private String titulo;
    private String descricao;
    private Integer userStoryID;

    public TaskDTO(String titulo, String descricao, Integer userStoryID) {
        this.descricao = descricao;
        this.titulo = titulo;
        this.userStoryID = userStoryID;
    }

    public Integer getUserStoryID() {
        return userStoryID;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTitulo() {
        return titulo;
    }

}
