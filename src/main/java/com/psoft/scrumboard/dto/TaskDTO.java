package com.psoft.scrumboard.dto;

public class TaskDTO {

    private String titulo;
    private String descricao;
    private Integer userStoryID;
    private String userName;

    public TaskDTO(String titulo, String descricao, Integer userStoryID, String userName) {
        this.descricao = descricao;
        this.titulo = titulo;
        this.userStoryID = userStoryID;
        this.userName = userName;
    }

    public Integer getUserStoryID() {
        return userStoryID;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUserName(){ return userName;}
}
