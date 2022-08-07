package com.psoft.scrumboard.repository;

import java.util.HashMap;
import java.util.Map;

import com.psoft.scrumboard.model.papel.Desenvolvedor;
import com.psoft.scrumboard.model.papel.Estagiario;
import com.psoft.scrumboard.model.papel.Papel;
import com.psoft.scrumboard.model.papel.Pesquisador;
import com.psoft.scrumboard.model.papel.ProductOwner;
import com.psoft.scrumboard.model.papel.ScrumMaster;

public class PapelRepository {
	
	private Map<Integer, Papel> papeis;
	
	public PapelRepository() {
		this.papeis = new HashMap<Integer, Papel>();
		this.papeis.put(0, new ScrumMaster());
		this.papeis.put(1, new ProductOwner());
		this.papeis.put(2, new Pesquisador());
		this.papeis.put(3, new Desenvolvedor());
		this.papeis.put(4, new Estagiario());
	}
	
	public Papel getPapelByID(Integer id) {
		return this.papeis.get(id);
	}

}
