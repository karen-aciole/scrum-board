package com.psoft.scrumboard.dto;

public class MudaStatusTaskDTO {
    Integer taskId;
    Integer idUserStory;
    String username;



    public MudaStatusTaskDTO(Integer taskId, Integer projectKey,  Integer idUserStory, String username){
        this.idUserStory = idUserStory;
        this.username = username;
        this.taskId = taskId;
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

}
