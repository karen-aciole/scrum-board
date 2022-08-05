package com.psoft.scrumboard.repository;

import com.psoft.scrumboard.model.Integrante;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class IntegranteRepository {

	private Map<String, Integrante> integrantes;

	public IntegranteRepository() {
		this.integrantes = new HashMap<String, Integrante>();
	}
	
	public void addUser(Integrante integrante) {
		this.integrantes.put(integrante.getUsuario().getUsername(), integrante);
	}
	
	public boolean containsUsername(String username) {
		return this.integrantes.containsKey(username);
	}
	
	public Integrante getUser(String username) {
		return this.integrantes.get(username);
	}
	
	public void delUser(String username) {
		this.integrantes.remove(username);
	}

}
