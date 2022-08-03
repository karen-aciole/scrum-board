package com.psoft.scrumboard.model;

import com.psoft.scrumboard.model.EstagioDeDesenvolvimento.EstagioDeDesenvolvimento;

public class UserStory {
    private String titulo;
    private String descricao;
    private EstagioDeDesenvolvimento estagioDeDesenvolvimento;

    public UserStory(String titulo, String descricao, EstagioDeDesenvolvimento estagioDeDesenvolvimento) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.estagioDeDesenvolvimento = estagioDeDesenvolvimento; // alterar para ToDo quando implementada
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EstagioDeDesenvolvimento getEstagioDeDesenvolvimento() {
        return estagioDeDesenvolvimento;
    }

    public void setEstagioDeDesenvolvimento(EstagioDeDesenvolvimento estagioDeDesenvolvimento) {
        this.estagioDeDesenvolvimento = estagioDeDesenvolvimento;
    }

    public String toString() {
        return "Título: " + this.titulo + "\n"
                + "Descrição: " + this.descricao;
    }

}
