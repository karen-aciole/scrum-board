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
        this.userStories.put(userStory.getId(), userStory);
    }

    public boolean containsUserStory(Integer id) {
        return this.userStories.containsKey(id);
    }

    public UserStory getUserStory(Integer id) {
        return this.userStories.get(id);
    }

    public void delUserStory(Integer id) {
        this.userStories.remove(id);
    }

}
