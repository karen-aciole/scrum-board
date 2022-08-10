package com.psoft.scrumboard.model.papel;

import com.psoft.scrumboard.model.papel.enums.PapelEnum;

public class ProductOwner implements Papel {

	public PapelEnum getTipo() {
		return PapelEnum.PRODUCT_OWNER;
	}
	
}
