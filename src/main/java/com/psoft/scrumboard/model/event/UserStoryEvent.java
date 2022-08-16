package com.psoft.scrumboard.model.event;

import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.repository.observer.UserStorySource;

public class UserStoryEvent {
	
	private UserStorySource userStorySource;
	
	private Integer projectKey;
	
	private UserStory userStory;
	
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

}
