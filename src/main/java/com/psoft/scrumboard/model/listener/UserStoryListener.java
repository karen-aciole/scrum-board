package com.psoft.scrumboard.model.listener;

import com.psoft.scrumboard.model.event.UserStoryEvent;

public interface UserStoryListener {
	
	public void mudouDescricao(UserStoryEvent userStoryEvent);
	
	public void mudouEstagio(UserStoryEvent userStoryEvent);
	
	public void marcouTaskRealizada(UserStoryEvent userStoryEvent);
	
	public void finalizouUserStory(UserStoryEvent userStoryEvent);

}
