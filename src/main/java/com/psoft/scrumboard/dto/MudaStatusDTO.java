package com.psoft.scrumboard.dto;

public class MudaStatusDTO {
    Integer projectKey;
    Integer idUserStory;
    String username;

    public MudaStatusDTO(Integer projectKey, Integer idUserStory, String username){
        this.idUserStory = idUserStory;
        this.username = username;
        this.projectKey = projectKey;
    }

    public Integer getProjectKey(){
        return this.projectKey;
    }

    public Integer getIdUserStory() {
        return idUserStory;
    }

    public String getUsername() {
        return username;
    }
}
