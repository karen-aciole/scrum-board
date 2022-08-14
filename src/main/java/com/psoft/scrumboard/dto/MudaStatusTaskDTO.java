package com.psoft.scrumboard.dto;

public class MudaStatusTaskDTO {
    Integer taskId;
    Integer projectKey;
    Integer idUserStory;
    String username;



    public MudaStatusTaskDTO(Integer taskId, Integer projectKey,  Integer idUserStory, String username){
        this.idUserStory = idUserStory;
        this.username = username;
        this.taskId = taskId;
        this.projectKey = projectKey;
    }

    public Integer getTaskKey(){
        return this.taskId;
    }

    public Integer getIdUserStory() {
        return idUserStory;
    }

    public String getUsername() {
        return username;
    }

    public Integer getProjectKey() {
        return projectKey;
    }
}
