package com.psoft.scrumboard.dto;

public class AtribuiUserStoryDTO {

    Integer projectKey;
    Integer idUserStory;
    String username;
    String scrumMasterName;

    public AtribuiUserStoryDTO(Integer projectKey, Integer idUserStory, String username, String scrumMasterName){
        this.idUserStory = idUserStory;
        this.scrumMasterName = scrumMasterName;
        this.username = username;
        this.projectKey = projectKey;
    }

    public Integer getProjectKey(){
        return this.projectKey;
    }

    public String getScrumMasterName() {
        return scrumMasterName;
    }

    public Integer getIdUserStory() {
        return idUserStory;
    }

    public String getUsername() {
        return username;
    }
}
