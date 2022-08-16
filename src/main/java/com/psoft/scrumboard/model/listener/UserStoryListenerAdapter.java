package com.psoft.scrumboard.model.listener;

import com.psoft.scrumboard.model.event.UserStoryEvent;

public abstract class UserStoryListenerAdapter implements UserStoryListener {
	
	private String username;
	
	public UserStoryListenerAdapter(String username) {
		this.username = username;
	}
	
	public void mudouDescricao(UserStoryEvent userStoryEvent) {
		// TODO Faz nada
	}
	
	public void mudouEstagio(UserStoryEvent userStoryEvent) {
		// TODO Faz nada
	}
	
	public void marcouTaskRealizada(UserStoryEvent userStoryEvent) {
		// TODO Faz nada
	}
	
	public void finalizouUserStory(UserStoryEvent userStoryEvent) {
		// TODO Faz nada
	}
	
	public String getUsername() {
		return this.username;
	}

}
