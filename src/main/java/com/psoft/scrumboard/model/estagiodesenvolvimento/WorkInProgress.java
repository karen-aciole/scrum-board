package com.psoft.scrumboard.model.estagiodesenvolvimento;

import com.psoft.scrumboard.model.enums.EstagioDesenvolvimentoEnum;

public class WorkInProgress implements EstagioDesenvolvimento{
    @Override
    public EstagioDesenvolvimentoEnum getTipo() {
        return EstagioDesenvolvimentoEnum.WORK_IN_PROGRESS;
    }
}
