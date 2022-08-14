package com.psoft.scrumboard.model;

public class Task {
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
        return this.descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getStatus(){
        if(this.status == false){
            return "Não realizada";
        } else {
            return "Realizada";
        }
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus() {
        if(this.status == false){
            this.status = true;
        } else {
            this.status = false;
        }
    }

    public void setUserStoryID(Integer userStoryID) {
        this.userStoryID = userStoryID;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String toString() {
        String userInfo = "Informações da task de titulo '" + this.titulo + "'\n"
                + "Descricao: " + this.descricao + "\n"
                + "userStory ID: " + this.userStoryID + "\n"
                + "Status: " + this.getStatus();

        return userInfo;
    }
}
