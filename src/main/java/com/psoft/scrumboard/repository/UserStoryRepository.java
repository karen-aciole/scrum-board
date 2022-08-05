package com.psoft.scrumboard.repository;

import com.psoft.scrumboard.model.UserStory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserStoryRepository {

    private Map<Integer, UserStory> userStories;

    public UserStoryRepository() {
        this.userStories = new HashMap<Integer, UserStory>();
    }

    public void addUserStory(UserStory userStory) {
        this.userStories.put(userStory.getNumero(), userStory);
    }

    public boolean containsUserStory(Integer numero) {
        return this.userStories.containsKey(numero);
    }

    public UserStory getUserStory(Integer numero) {
        return this.userStories.get(numero);
    }

    public void delUserStory(Integer numero) {
        this.userStories.remove(numero);
    }

}
