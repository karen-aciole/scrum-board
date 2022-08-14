package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.MudaStatusTaskDTO;
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


    public String deletaTask(Integer userStoryID, Integer taskid) throws UserStoryNotFoundException {
        UserStory us = getUs(userStoryID);
        us.getTasks().delTask(taskid);

        return "Task deletada com sucesso";
    }

    public String getInfoTask(Integer userStoryID, Integer taskID) throws UserStoryNotFoundException {
        UserStory us = getUs(userStoryID);
        return us.getTasks().getTask(taskID).toString();
    }

    public String updateInfoTask(Integer taskId, TaskDTO taskDTO) throws UserStoryNotFoundException {
        UserStory us = getUs(taskDTO.getUserStoryID());

        us.getTasks().getTask(taskId).setDescricao(taskDTO.getDescricao());
        us.getTasks().getTask(taskId).setTitulo(taskDTO.getTitulo());

        return us.getTasks().getTask(taskId).toString();
    }

    public UserStory getUs(Integer userStoryId) throws UserStoryNotFoundException {
        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(userStoryId);

        if (projeto.equals(null)) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        }

        UserStory us = projeto.getUserStory(userStoryId);

        return us;
    }

    public Task getTask(Integer taskId, Integer userStoryId) throws UserStoryNotFoundException, TaskNotFoundException {
        UserStory us = getUs(userStoryId);
        Task task = us.getTasks().getTask(taskId);

        if (task.equals(null)) {
            throw new TaskNotFoundException("Task não está cadastrada no sistema - id inválido.");
        }

        return task;
    }

    public String mudaStatusTask(MudaStatusTaskDTO mudaStatusTaskDTO) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {
        Task task = getTask(mudaStatusTaskDTO.getTaskKey(), mudaStatusTaskDTO.getIdUserStory());
        UserStory us = getUs(mudaStatusTaskDTO.getIdUserStory());
        String scrumMasterName = this.projetoService.getScrumMasterName(mudaStatusTaskDTO.getProjectKey());


        if (!us.getResponsaveis().containsUsername(mudaStatusTaskDTO.getUsername()) && !scrumMasterName.equals(mudaStatusTaskDTO.getUsername())) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        task.setStatus();

        return "Status alterado com sucesso";
    }
}
