package com.psoft.scrumboard.dto;

public class UserStoryDTO {
	
	private Integer numero;

    private String titulo;
    
    private String descricao;
    
    public UserStoryDTO(Integer numero, String titulo, String descricao) {
        this.numero = numero;
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public Integer getNumero() { 
    	return this.numero;
    }
    
    public String getTitulo() {
    	return this.titulo;
    }
    
    public String getDescricao() {
        return this.descricao;
    }

}
