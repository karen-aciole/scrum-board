package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.MudaStatusTaskDTO;
import com.psoft.scrumboard.dto.TaskDTO;
import com.psoft.scrumboard.exception.*;
import com.psoft.scrumboard.model.Projeto;
import com.psoft.scrumboard.model.Task;
import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.model.event.UserStoryEvent;
import com.psoft.scrumboard.repository.observer.UserStorySource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class TaskService {

    @Autowired
    private UserStoryService userStoryService;
    
    @Autowired
    private ProjetoService projetoService;
    
    @Autowired
    private UserStorySource userStorySource;
    
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


    public String deletaTask(Integer taskId, Integer idUserStory, String userName) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {
        UserStory us = getUs(idUserStory);
        Task task = getTask(taskId, idUserStory);
        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(idUserStory);

        if (task.equals(null)) {
            throw new TaskNotFoundException("Task não está cadastrada no sistema - id inválido.");
        }else if (us.equals(null)) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        }else if (!us.getResponsaveis().containsUsername(userName) && !projeto.getScrumMaster().getUsuario().getUsername().equals(userName)) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        us.getTasks().delTask(taskId);

        return "Task deletada com sucesso";
    }

    public String getInfoTask(Integer taskId, Integer idUserStory, String userName) throws TaskNotFoundException, UserStoryNotFoundException, UsuarioNotAllowedException {
        UserStory us = getUs(idUserStory);
        System.out.println(us.getTitulo());
        Task task = getTask(taskId, idUserStory);
        System.out.println(task.toString());
        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(idUserStory);
        System.out.println(projeto.getNome());

        if (task.equals(null)) {
            throw new TaskNotFoundException("Task não está cadastrada no sistema - id inválido.");
        }else if (us.equals(null)) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        } else if (!us.getResponsaveis().containsUsername(userName) && !projeto.getScrumMaster().getUsuario().getUsername().equals(userName)) {
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

    public String mudaStatusTask(Integer taskId, Integer idUserStory, String userName) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {
        Task task = getTask(taskId, idUserStory);
        UserStory us = getUs(idUserStory);
        Projeto projeto = this.projetoService.encontraProjetoPorIDUserStory(idUserStory);
        String scrumMasterName = projeto.getScrumMaster().getUsuario().getUsername();

        if (!us.getResponsaveis().containsUsername(userName) && !scrumMasterName.equals(userName)) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        task.setStatus();
        this.userStorySource.marcouTaskRealizada(taskId, task.getStatus());

        return "Status alterado com sucesso";
    }
}
