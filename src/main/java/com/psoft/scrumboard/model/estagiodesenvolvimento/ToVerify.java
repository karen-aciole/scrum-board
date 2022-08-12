package com.psoft.scrumboard.model.estagiodesenvolvimento;

import com.psoft.scrumboard.model.enums.EstagioDesenvolvimentoEnum;

public class ToVerify implements EstagioDesenvolvimento{

    @Override
    public EstagioDesenvolvimentoEnum getTipo() {
        return EstagioDesenvolvimentoEnum.TO_VERIFY;
    }
}
