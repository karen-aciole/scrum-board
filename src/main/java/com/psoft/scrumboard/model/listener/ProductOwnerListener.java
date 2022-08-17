package com.psoft.scrumboard.model.listener;

import com.psoft.scrumboard.model.event.UserStoryEvent;

public class ProductOwnerListener extends UserStoryListenerAdapter {

	public ProductOwnerListener(String username) {
		super(username);
	}
	
	public void finalizouUserStory(UserStoryEvent userStoryEvent) {
		
		String notificacao = "\nNotificação para o Product Owner com username '" + super.getUsername() + "'\n"
				+ "Atualização na US" + userStoryEvent.getUserStory().getId() + " do projeto com ID '" + userStoryEvent.getProjectKey() + "'\n"
				+ "- User Story foi finalizada (marcada como Done)\n";
		
		System.out.println(notificacao);
	}

}
