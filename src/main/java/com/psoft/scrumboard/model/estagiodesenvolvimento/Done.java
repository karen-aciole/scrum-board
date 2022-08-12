package com.psoft.scrumboard.model.estagiodesenvolvimento;

import com.psoft.scrumboard.model.enums.EstagioDesenvolvimentoEnum;

public class Done implements EstagioDesenvolvimento{

    public EstagioDesenvolvimentoEnum getTipo() {
        return EstagioDesenvolvimentoEnum.DONE;
    }
}
