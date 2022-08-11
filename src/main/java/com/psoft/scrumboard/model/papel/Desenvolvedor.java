package com.psoft.scrumboard.model.papel;

import com.psoft.scrumboard.model.enums.PapelEnum;

public class Desenvolvedor implements Papel {
	
	public PapelEnum getTipo() {
		return PapelEnum.DESENVOLVEDOR;
	}
	
}
