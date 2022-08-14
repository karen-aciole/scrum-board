package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.MudaStatusTaskDTO;
import com.psoft.scrumboard.dto.ReadDeleteTaskDTO;
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
    public int criaTask(TaskDTO taskDTO) throws TaskAlreadyExistsException, UserStoryNotFoundException, UsuarioNotAllowedException {

        Task task = new Task(taskDTO.getTitulo(), taskDTO.getDescricao(), taskDTO.getUserStoryID());
        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(taskDTO.getUserStoryID());
        UserStory us = getUs(taskDTO.getUserStoryID());

        if (us.equals(null)) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        }else if (!us.getResponsaveis().containsUsername(taskDTO.getUserName()) && !projeto.getScrumMaster().getUsuario().getUsername().equals(taskDTO.getUserName())) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        int chave = projeto.getUserStory(taskDTO.getUserStoryID()).getTasks().addTask(task);


        return chave;
    }


    public String deletaTask(ReadDeleteTaskDTO deletaTaskDTO) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {
        UserStory us = getUs(deletaTaskDTO.getUserStoryID());
        Task task = getTask(deletaTaskDTO.getTaskId(), deletaTaskDTO.getUserStoryID());
        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(deletaTaskDTO.getUserStoryID());


        if (task.equals(null)) {
            throw new TaskNotFoundException("Task não está cadastrada no sistema - id inválido.");
        }else if (us.equals(null)) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        }else if (!us.getResponsaveis().containsUsername(deletaTaskDTO.getUserName()) && !projeto.getScrumMaster().getUsuario().getUsername().equals(deletaTaskDTO.getUserName())) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        us.getTasks().delTask(deletaTaskDTO.getTaskId());

        return "Task deletada com sucesso";
    }

    public String getInfoTask(ReadDeleteTaskDTO readDeleteTaskDTO) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {
        UserStory us = getUs(readDeleteTaskDTO.getUserStoryID());
        Task task = us.getTasks().getTask(readDeleteTaskDTO.getTaskId());
        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(readDeleteTaskDTO.getUserStoryID());

        if (task.equals(null)) {
            throw new TaskNotFoundException("Task não está cadastrada no sistema - id inválido.");
        }else if (us.equals(null)) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        } else if (!us.getResponsaveis().containsUsername(readDeleteTaskDTO.getUserName()) && !projeto.getScrumMaster().getUsuario().getUsername().equals(readDeleteTaskDTO.getUserName())) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }


        return task.toString();
    }

    public String updateInfoTask(Integer taskId, TaskDTO taskDTO) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {
        UserStory us = getUs(taskDTO.getUserStoryID());
        Task task = us.getTasks().getTask(taskId);
        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(taskDTO.getUserStoryID());

        if (task.equals(null)) {
            throw new TaskNotFoundException("Task não está cadastrada no sistema - id inválido.");
        }else if (us.equals(null)) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        } else if (!us.getResponsaveis().containsUsername(taskDTO.getUserName()) && !projeto.getScrumMaster().getUsuario().getUsername().equals(taskDTO.getUserName())) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        task.setDescricao(taskDTO.getDescricao());
        task.setTitulo(taskDTO.getTitulo());

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
