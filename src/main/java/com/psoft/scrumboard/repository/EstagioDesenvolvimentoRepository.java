package com.psoft.scrumboard.repository;

import java.util.HashMap;
import java.util.Map;

import com.psoft.scrumboard.model.enums.EstagioDesenvolvimentoEnum;
import com.psoft.scrumboard.model.estagiodesenvolvimento.Done;
import com.psoft.scrumboard.model.estagiodesenvolvimento.EstagioDesenvolvimento;
import com.psoft.scrumboard.model.estagiodesenvolvimento.ToDo;
import com.psoft.scrumboard.model.estagiodesenvolvimento.ToVerify;
import com.psoft.scrumboard.model.estagiodesenvolvimento.WorkInProgress;

public class EstagioDesenvolvimentoRepository {
	
	private Map<EstagioDesenvolvimentoEnum, EstagioDesenvolvimento> estagiosDesenvolvimento;
	
	public EstagioDesenvolvimentoRepository() {
		this.estagiosDesenvolvimento = new HashMap<EstagioDesenvolvimentoEnum, EstagioDesenvolvimento>();
		this.estagiosDesenvolvimento.put(EstagioDesenvolvimentoEnum.TO_DO, new ToDo());
		this.estagiosDesenvolvimento.put(EstagioDesenvolvimentoEnum.WORK_IN_PROGRESS, new WorkInProgress());
		this.estagiosDesenvolvimento.put(EstagioDesenvolvimentoEnum.TO_VERIFY, new ToVerify());
		this.estagiosDesenvolvimento.put(EstagioDesenvolvimentoEnum.DONE, new Done());
	}
	
	public EstagioDesenvolvimento getEstagioDesenvolvimentoByEnum(EstagioDesenvolvimentoEnum estagio) {
		return this.estagiosDesenvolvimento.get(estagio);
	}

}
