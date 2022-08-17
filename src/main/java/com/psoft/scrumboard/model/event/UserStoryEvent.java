package com.psoft.scrumboard.model.event;

import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.repository.observer.UserStorySource;

public class UserStoryEvent {
	
	private UserStorySource userStorySource;
	
	private Integer projectKey;
	
	private UserStory userStory;
	
	private Integer taskId;
	
	private String taskStatus;
	
	public UserStoryEvent(UserStorySource userStorySource, Integer projectKey, UserStory userStory) {
		this.userStorySource = userStorySource;
		this.projectKey = projectKey;
		this.userStory = userStory;
	}
	
	public UserStorySource getUserStorySource() {
		return this.userStorySource;
	}
	
	public UserStory getUserStory() {
		return this.userStory;
	}
	
	public Integer getProjectKey() {
		return this.projectKey;
	}
	
	public void setTaskID(Integer taskId) {
		this.taskId = taskId;
	}
	
	public Integer getTaskID() {
		return this.taskId;
	}
	
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	public String getTaskStatus() {
		return this.taskStatus;
	}

}
