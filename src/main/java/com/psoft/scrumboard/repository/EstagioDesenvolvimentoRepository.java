package com.psoft.scrumboard.repository;

import java.util.HashMap;
import java.util.Map;

import com.psoft.scrumboard.model.estagiodesenvolvimento.Done;
import com.psoft.scrumboard.model.estagiodesenvolvimento.EstagioDesenvolvimento;
import com.psoft.scrumboard.model.estagiodesenvolvimento.ToDo;
import com.psoft.scrumboard.model.estagiodesenvolvimento.ToVerify;
import com.psoft.scrumboard.model.estagiodesenvolvimento.WorkInProgress;

public class EstagioDesenvolvimentoRepository {
	
	private Map<Integer, EstagioDesenvolvimento> estagiosDesenvolvimento;
	
	public EstagioDesenvolvimentoRepository() {
		this.estagiosDesenvolvimento = new HashMap<Integer, EstagioDesenvolvimento>();
		this.estagiosDesenvolvimento.put(1, new ToDo());
		this.estagiosDesenvolvimento.put(2, new WorkInProgress());
		this.estagiosDesenvolvimento.put(3, new ToVerify());
		this.estagiosDesenvolvimento.put(4, new Done());
	}
	
	public EstagioDesenvolvimento getEstagioDesenvolvimentoByID(Integer id) {
		return this.estagiosDesenvolvimento.get(id);
	}

}
