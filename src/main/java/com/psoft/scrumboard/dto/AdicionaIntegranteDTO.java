package com.psoft.scrumboard.dto;

import com.psoft.scrumboard.model.papel.enums.PapelEnum;

public class AdicionaIntegranteDTO {
    private Integer projectKey;
    private PapelEnum papel;
    private String userName;
    private String scrumMasterName;


    public AdicionaIntegranteDTO(Integer projectKey, PapelEnum papel, String userName, String scrumMasterName) {
        this.projectKey = projectKey;
        this.papel = papel;
        this.userName = userName;
        this.scrumMasterName = scrumMasterName;
    }

    public Integer getProjectKey() {
        return this.projectKey;
    }

    public PapelEnum getPapel() {
        return this.papel;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getScrumMasterName() {
        return this.scrumMasterName;
    }
}
