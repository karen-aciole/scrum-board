package com.psoft.scrumboard.model.estagiodesenvolvimento;

import com.psoft.scrumboard.model.enums.EstagioDesenvolvimentoEnum;

public class ToDo implements EstagioDesenvolvimento{
    @Override
    public EstagioDesenvolvimentoEnum getTipo() {
        return EstagioDesenvolvimentoEnum.TO_DO;
    }
}
