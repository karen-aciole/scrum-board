package com.psoft.scrumboard.model;

public class Usuario {
	
	private String nomeCompleto;
	
	private String username;
	
	private String email;
	
	private String senha;
	
	private Papel papel;
	

	public Usuario(String nomeCompleto, String username, String email, String senha) {
		this.nomeCompleto = nomeCompleto;
		this.username = username;
		this.email = email;
		this.senha = senha;
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
	
	public String getSenha() {
		return this.senha;
	}
	
	public String toString() {
		
		String userInfo = "Informações do usuário com username '" + this.username + "'\n"
				+ "Nome completo: " + this.nomeCompleto + "\n"
				+ "E-mail: " + this.email;
		
		return userInfo;
	}

}
