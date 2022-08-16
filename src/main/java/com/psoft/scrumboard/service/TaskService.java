package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.MudaStatusTaskDTO;
import com.psoft.scrumboard.dto.TaskDTO;
import com.psoft.scrumboard.exception.*;
import com.psoft.scrumboard.model.Projeto;
import com.psoft.scrumboard.model.Task;
import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.repository.ProjetoRepository;
import com.psoft.scrumboard.repository.UserStoryRepository;
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
    private ProjetoRepository projetoRepository;
    @Autowired
    private UserStoryRepository userStoryRepository;

    public int criaTask(Integer projectKey, TaskDTO taskDTO) throws TaskAlreadyExistsException, UserStoryNotFoundException, UsuarioNotAllowedException {

        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        UserStory userStory = projeto.getUserStory(taskDTO.getUserStoryID());

        if (userStory == null) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        }else if (!userStory.getResponsaveis().containsUsername(taskDTO.getUserName()) && !projeto.getScrumMaster().getUsuario().getUsername().equals(taskDTO.getUserName())) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        Task task = new Task(taskDTO.getTitulo(), taskDTO.getDescricao(), taskDTO.getUserStoryID());

        return userStory.getTasks().addTask(task);
    }


    public String deletaTask(Integer taskId, Integer idUserStory, String userName, Integer projectKey) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {
        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        UserStory userStory = projeto.getUserStory(idUserStory);
        Task task = getTask(taskId, idUserStory, projectKey);

        if (userStory == null) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        }else if (!userStory.getResponsaveis().containsUsername(userName) && !projeto.getScrumMaster().getUsuario().getUsername().equals(userName)) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        userStory.getTasks().delTask(taskId);

        return "Task deletada com sucesso";
    }

    public String getInfoTask(Integer taskId, Integer idUserStory, String userName, Integer projectKey)
            throws TaskNotFoundException, UserStoryNotFoundException, UsuarioNotAllowedException, ProjetoNotFoundException {

        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        UserStory userStory = projeto.getUserStory(idUserStory);
        Task task = getTask(taskId, idUserStory, projectKey);

        if (projeto == null) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - id inválido.");
        } else if (userStory == null) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        } else if (!userStory.getResponsaveis().containsUsername(userName) && !projeto.getScrumMaster().getUsuario().getUsername().equals(userName)) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        return task.toString();
    }

    public String updateInfoTask(Integer taskId, Integer projectKey, TaskDTO taskDTO) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {
        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        UserStory userStory = projeto.getUserStory(taskDTO.getUserStoryID());
        Task task = userStory.getTasks().getTask(taskId);

        if (task == null) {
            throw new TaskNotFoundException("Task não está cadastrada no sistema - id inválido.");
        }else if (!userStory.getResponsaveis().containsUsername(taskDTO.getUserName()) && !projeto.getScrumMaster().getUsuario().getUsername().equals(taskDTO.getUserName())) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        task.setDescricao(!taskDTO.getDescricao().isBlank() ? taskDTO.getDescricao() : task.getDescricao());
        task.setTitulo(!taskDTO.getTitulo().isBlank() ? taskDTO.getTitulo() : task.getTitulo());

        return userStory.getTasks().getTask(taskId).toString();
    }

    private Task getTask(Integer taskId, Integer userStoryId, Integer projetoKey) throws TaskNotFoundException {
        Projeto projeto = this.projetoRepository.getProjeto(projetoKey);
        UserStory userStory = projeto.getUserStory(userStoryId);
        Task task = userStory.getTasks().getTask(taskId);

        if (task == null) {
            throw new TaskNotFoundException("Task não está cadastrada no sistema - id inválido.");
        }

        return task;
    }

    public String mudaStatusTask(Integer taskId, Integer idUserStory, String userName, Integer projetoKey) throws TaskNotFoundException, UsuarioNotAllowedException {
        Projeto projeto = this.projetoRepository.getProjeto(projetoKey);
        UserStory userStory = projeto.getUserStory(idUserStory);
        Task task = getTask(taskId, idUserStory, projetoKey);
        String scrumMasterName = projeto.getScrumMaster().getUsuario().getUsername();

        if (!userStory.getResponsaveis().containsUsername(userName) && !scrumMasterName.equals(userName)) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        task.setStatus();

        return "Status alterado com sucesso";
    }
}
