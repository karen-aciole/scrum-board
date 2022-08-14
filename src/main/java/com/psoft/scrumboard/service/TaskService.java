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


        return chave;
    }


    public String deletaTask(Integer userStoryID, Integer taskid) {
        UserStory us = getUs(userStoryID);
        us.getTasks().delTask(taskid);

        return "Task deletada com sucesso";
    }

    public String getInfoTask(Integer userStoryID, Integer taskID) {
        UserStory us = getUs(userStoryID);
        return us.getTasks().getTask(taskID).toString();
    }

    public String updateInfoTask(Integer taskId, TaskDTO taskDTO) {
        UserStory us = getUs(taskDTO.getUserStoryID());

        us.getTasks().getTask(taskId).setDescricao(taskDTO.getDescricao());
        us.getTasks().getTask(taskId).setTitulo(taskDTO.getTitulo());

        return us.getTasks().getTask(taskId).toString();
    }

    public UserStory getUs(Integer userStoryId){
        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(userStoryId);
        UserStory us = projeto.getUserStory(userStoryId);

        return us;
    }
}
