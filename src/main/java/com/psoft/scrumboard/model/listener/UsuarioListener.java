package com.psoft.scrumboard.model.listener;

import com.psoft.scrumboard.model.event.UserStoryEvent;

public class UsuarioListener extends UserStoryListenerAdapter {
	
	public UsuarioListener(String username) {
		super(username);
	}

	public void mudouDescricao(UserStoryEvent userStoryEvent) {
		
		String notificacao = "\nNotificação para o usuário '" + super.getUsername() + "'\n"
				+ "Atualização na US" + userStoryEvent.getUserStory().getId() + " do projeto com ID '" + userStoryEvent.getProjectKey() + "'\n"
				+ "Mudança de descrição\n"
				+ "- Nova descrição: " + userStoryEvent.getUserStory().getDescricao() + "\n";
		
		System.out.println(notificacao);
	}
	
	public void mudouEstagio(UserStoryEvent userStoryEvent) {
		
		String notificacao = "\nNotificação para o usuário '" + super.getUsername() + "'\n"
				+ "Atualização na US" + userStoryEvent.getUserStory().getId() + " do projeto com ID '" + userStoryEvent.getProjectKey() + "'\n"
				+ "Mudança de estágio\n"
				+ "- Mudou para o estágio " + userStoryEvent.getUserStory().getEstagioDesenvolvimento().name() + "\n";
		
		System.out.println(notificacao);
	}

}
