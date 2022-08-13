package com.psoft.scrumboard.dto;

public class UsuarioDTO {
	
	private String nomeCompleto;
	
	private String username;
	
	private String email;
	
	public UsuarioDTO(String nomeCompleto, String username, String email) {
		this.nomeCompleto = nomeCompleto;
		this.username = username;
		this.email = email;
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

}
