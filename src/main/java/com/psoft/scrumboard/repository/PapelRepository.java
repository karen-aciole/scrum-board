package com.psoft.scrumboard.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.psoft.scrumboard.model.papel.Desenvolvedor;
import com.psoft.scrumboard.model.papel.Estagiario;
import com.psoft.scrumboard.model.papel.Papel;
import com.psoft.scrumboard.model.papel.Pesquisador;
import com.psoft.scrumboard.model.papel.ProductOwner;
import com.psoft.scrumboard.model.papel.ScrumMaster;
import com.psoft.scrumboard.model.enums.PapelEnum;

public class PapelRepository {
	
	private Map<PapelEnum, Papel> papeis;
	
	public PapelRepository() {
		this.papeis = new HashMap<PapelEnum, Papel>();
		this.papeis.put(PapelEnum.SCRUM_MASTER, new ScrumMaster());
		this.papeis.put(PapelEnum.PRODUCT_OWNER, new ProductOwner());
		this.papeis.put(PapelEnum.PESQUISADOR, new Pesquisador());
		this.papeis.put(PapelEnum.DESENVOLVEDOR, new Desenvolvedor());
		this.papeis.put(PapelEnum.ESTAGIARIO, new Estagiario());
	}
	
	public Papel getPapelByEnum(PapelEnum papel) {
		return this.papeis.get(papel);
	}
	
	public Collection<Papel> getPapeis() {
		return this.papeis.values();
	}

}
