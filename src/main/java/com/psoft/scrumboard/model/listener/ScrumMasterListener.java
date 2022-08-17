package com.psoft.scrumboard.model.listener;

import com.psoft.scrumboard.model.event.UserStoryEvent;

public class ScrumMasterListener extends UserStoryListenerAdapter {

	public ScrumMasterListener(String username) {
		super(username);
	}
	
public void mudouDescricao(UserStoryEvent userStoryEvent) {
		
		String notificacao = "\nNotificação para o Scrum Master com username '" + super.getUsername() + "'\n"
				+ "Atualização na US" + userStoryEvent.getUserStory().getId() + " do projeto com ID '" + userStoryEvent.getProjectKey() + "'\n"
				+ "Mudança de descrição\n"
				+ "- Nova descrição: " + userStoryEvent.getUserStory().getDescricao() + "\n";
		
		System.out.println(notificacao);
	}
	
	public void mudouEstagio(UserStoryEvent userStoryEvent) {
		
		String notificacao = "\nNotificação para o Scrum Master com username '" + super.getUsername() + "'\n"
				+ "Atualização na US" + userStoryEvent.getUserStory().getId() + " do projeto com ID '" + userStoryEvent.getProjectKey() + "'\n"
				+ "Mudança de estágio\n"
				+ "- Mudou para o estágio " + userStoryEvent.getUserStory().getEstagioDesenvolvimento().name() + "\n";
		
		System.out.println(notificacao);
	}
	
	public void marcouTaskRealizada(UserStoryEvent userStoryEvent) {
		
		String notificacao = "\nNotificação para o Scrum Master com username '" + super.getUsername() + "'\n"
				+ "Atualização na task com ID '" + userStoryEvent.getTaskID() + "'\n"
				+ "Status da task\n"
				+ "- " + userStoryEvent.getTaskStatus() + "\n";
		
		System.out.println(notificacao);
	}
	
}
