package com.psoft.scrumboard.model;

import com.psoft.scrumboard.model.papel.Papel;

public class Usuario {
	
	private String nomeCompleto;
	
	private String username;
	
	private String email;
	
	private Papel papel;
	

	public Usuario(String nomeCompleto, String username, String email) {
		this.nomeCompleto = nomeCompleto;
		this.username = username;
		this.email = email;
	}
	
	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
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
	
	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String toString() {
		
		String userInfo = "Informações do usuário com username '" + this.username + "'\n"
				+ "Nome completo: " + this.nomeCompleto + "\n"
				+ "E-mail: " + this.email;
		
		return userInfo;
	}

}
