package com.psoft.scrumboard.repository;

import com.psoft.scrumboard.model.UserStory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserStoryRepository {

    private Map<String, UserStory> userStories;

    public UserStoryRepository() {
        this.userStories = new HashMap<String, UserStory>();
    }

    public void addUserStory(UserStory userStory) {
        this.userStories.put(userStory.getTitulo(), userStory);
    }

    public boolean containsUserStory(String titulo) {
        return this.userStories.containsKey(titulo);
    }

    public UserStory getUserStory(String titulo) {
        return this.userStories.get(titulo);
    }

    public void delUserStory(String titulo) {
        this.userStories.remove(titulo);
    }

}
