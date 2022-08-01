package com.psoft.scrumboard.dto;

public class UsuarioDTO {
	
	private String nomeCompleto;
	
	private String username;
	
	private String email;
	
	private String senha;
	
	public UsuarioDTO(String nomeCompleto, String username, String email, String senha) {
		this.nomeCompleto = nomeCompleto;
		this.username = username;
		this.email = email;
		this.senha = senha;
	}
	
	public String getNomeCompleto() {
		return this.nomeCompleto;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getSenha() {
		return this.senha;
	}

}
