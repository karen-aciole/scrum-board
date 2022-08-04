package com.psoft.scrumboard.model;

import com.psoft.scrumboard.model.estagioDesenvolvimento.EstagioDesenvolvimento;

public class UserStory {
	
    private String titulo;
    
    private String descricao;
    
    private String estagioDesenvolvimento;

    public UserStory(String titulo, String descricao, String estagioDesenvolvimento) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.estagioDesenvolvimento = estagioDesenvolvimento; // o estágio é TODO por default
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

    public String getEstagioDesenvolvimento() {
        return this.estagioDesenvolvimento;
    }

    public void setEstagioDesenvolvimento(String estagioDesenvolvimento) {
        this.estagioDesenvolvimento = estagioDesenvolvimento;
    }

    public String toString() {
        return "Informações da UserStory '" + this.titulo + "'\n"
                + "Descrição: " + this.descricao + "\n"
                + "Estágio de desenvolvimento: " + this.estagioDesenvolvimento;
    }

}
