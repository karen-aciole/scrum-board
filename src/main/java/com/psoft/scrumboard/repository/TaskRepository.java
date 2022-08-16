package com.psoft.scrumboard.repository;

import com.psoft.scrumboard.model.Task;
import com.psoft.scrumboard.model.UserStory;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TaskRepository {

    private Map<Integer, Task> tasks;

    public TaskRepository() {
        this.tasks = new HashMap<Integer, Task>();
    }

    public int addTask(Task task) {
        Random random = new Random();

        Integer key = random.nextInt(20000000);
        this.tasks.put(key, task);

        return key;
    }

    public boolean containsTask(Integer id) {
        return this.tasks.containsKey(id);
    }

    public boolean containsTaskPorTitulo(String titulo) {
        for(Task task: this.tasks.values()){
            if(task.getTitulo().equals(titulo)){
                return true;
            }
        }
        return false;
    }

    public Task getTask(Integer id) {
       return this.tasks.get(id);
    }

    public void delTask(Integer id) {
        this.tasks.remove(id);
    }

    public Collection<Task> getAllTasksByIdUserStory(Integer idUserStory) {
        Collection<Task> tasks = new ArrayList<>();
        for (Task task: this.tasks.values())
            if (task.getUserStoryID().equals(idUserStory))
                tasks.add(task);

        return tasks;
    }
}
