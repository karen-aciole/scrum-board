package com.psoft.scrumboard.model;

public class Task {
    private Integer id;
    private String titulo;
    private String descricao;
    private Integer userStoryID;
    private boolean status;

    public Task(String titulo, String descricao, Integer userStoryID) {
        this.descricao = descricao;
        this.titulo = titulo;
        this.userStoryID = userStoryID;
        this.status = false;
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

    public boolean getStatus(){
        return status;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setUserStoryID(Integer userStoryID) {
        this.userStoryID = userStoryID;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
