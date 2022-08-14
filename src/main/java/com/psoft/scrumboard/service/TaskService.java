package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.TaskDTO;
import com.psoft.scrumboard.exception.*;
import com.psoft.scrumboard.model.Projeto;
import com.psoft.scrumboard.model.Task;
import com.psoft.scrumboard.model.UserStory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private UserStoryService userStoryService;
    @Autowired
    private ProjetoService projetoService;
    public int criaTask(TaskDTO taskDTO) throws TaskAlreadyExistsException {

        Task task = new Task(taskDTO.getTitulo(), taskDTO.getDescricao(), taskDTO.getUserStoryID());

        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(taskDTO.getUserStoryID());

        int chave = projeto.getUserStory(taskDTO.getUserStoryID()).getTasks().addTask(task);

        if (projeto.getUserStory(taskDTO.getUserStoryID()).getTasks().containsTaskPorTitulo(taskDTO.getTitulo())) {
            throw new TaskAlreadyExistsException("Uma task com o mesmo titulo já estra cadastrada nessa US");
        }

        return chave;
    }


    public String deletaTask(Integer userStoryID, Integer taskid) {
        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(userStoryID);

        UserStory us = projeto.getUserStory(userStoryID);
        us.getTasks().delTask(taskid);

        return "Task deletada com sucesso";
    }

    public String getInfoTask(Integer userStoryID, Integer taskID) {
        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(userStoryID);

        UserStory us = projeto.getUserStory(userStoryID);
        return us.getTasks().getTask(taskID).toString();
    }

    public String updateInfoTask(Integer taskId, TaskDTO taskDTO) {
        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(taskDTO.getUserStoryID());

        UserStory us = projeto.getUserStory(taskDTO.getUserStoryID());
        return us.getTasks().getTask(taskId).toString();
    }
}
