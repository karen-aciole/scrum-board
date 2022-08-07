package com.psoft.scrumboard.dto;

public class UserStoryDTO {
	
	private Integer id;

    private String titulo;
    
    private String descricao;
    
    public UserStoryDTO(Integer id, String titulo, String descricao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public Integer getId() {
    	return this.id;
    }
    
    public String getTitulo() {
    	return this.titulo;
    }
    
    public String getDescricao() {
        return this.descricao;
    }

}
