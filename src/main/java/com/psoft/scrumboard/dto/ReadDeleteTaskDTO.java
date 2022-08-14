package com.psoft.scrumboard.dto;

//DTO utilizado para as operações de Read e Delete
public class ReadDeleteTaskDTO {

    Integer userStoryID;
    String username;
    Integer taskId;

    public ReadDeleteTaskDTO(Integer userStoryID, Integer taskId, String username){
        this.taskId =taskId;
        this.userStoryID = userStoryID;
        this.username = username;
    }

    public Integer getUserStoryID() {
        return userStoryID;
    }

    public String getUserName() {
        return username;
    }

    public Integer getTaskId() {
        return taskId;
    }
}
