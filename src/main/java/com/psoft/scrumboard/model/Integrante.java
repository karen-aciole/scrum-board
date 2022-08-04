package com.psoft.scrumboard.model;

public class Integrante {
	
	private Usuario usuario;
	
	private Papel papel;
	
	public Integrante(Usuario usuario, Papel papel) {
		this.usuario = usuario;
		this.papel = papel;
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}
	
	public Papel getPapel() {
		return this.papel;
	}
	
}
