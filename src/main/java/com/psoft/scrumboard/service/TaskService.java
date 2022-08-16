package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.MudaStatusDTO;
import com.psoft.scrumboard.dto.MudaStatusTaskDTO;
import com.psoft.scrumboard.dto.TaskDTO;
import com.psoft.scrumboard.exception.*;
import com.psoft.scrumboard.model.Projeto;
import com.psoft.scrumboard.model.Task;
import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.model.enums.EstagioDesenvolvimentoEnum;
import com.psoft.scrumboard.model.estagiodesenvolvimento.EstagioDesenvolvimento;
import com.psoft.scrumboard.repository.EstagioDesenvolvimentoRepository;
import com.psoft.scrumboard.repository.ProjetoRepository;
import com.psoft.scrumboard.repository.TaskRepository;
import com.psoft.scrumboard.repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
    @Autowired
    private TaskRepository taskRepository;
    private EstagioDesenvolvimentoRepository estagioDesenvolvimentoRepository;

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

    public String mudaStatusTask(MudaStatusTaskDTO mudaStatusTaskDTO, Integer projetoKey)
            throws TaskNotFoundException, UsuarioNotAllowedException, ProjetoNotFoundException, StatusException, UserStoryNotFoundException, UsuarioNotFoundException {

        Projeto projeto = this.projetoRepository.getProjeto(projetoKey);
        UserStory userStory = projeto.getUserStory(mudaStatusTaskDTO.getIdUserStory());
        Task task = getTask(mudaStatusTaskDTO.getTaskKey(), mudaStatusTaskDTO.getIdUserStory(), projetoKey);
        String scrumMasterName = projeto.getScrumMaster().getUsuario().getUsername();

        if (!userStory.getResponsaveis().containsUsername(mudaStatusTaskDTO.getUsername()) && !scrumMasterName.equals(mudaStatusTaskDTO.getUsername())) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        task.setStatus();

        if (isAllTasksFinished(projetoKey, userStory.getId())) {
            this.userStoryService.mudaStatusWorkInProgressParaToVerify(new MudaStatusDTO(projetoKey, mudaStatusTaskDTO.getIdUserStory(), mudaStatusTaskDTO.getUsername()));
        }

        return "Status alterado com sucesso";
    }

    private boolean isAllTasksFinished(Integer projectKey, Integer idUserStory) {
        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        UserStory userStory = projeto.getUserStory(idUserStory);
        Collection<Task> listTasks = userStory.getTasks().getAllTasksByIdUserStory(idUserStory);

        int tasksFinalizadas = 0;
        for (Task task : listTasks)
            if (task.getStatus().equals("Realizada"))
                tasksFinalizadas++;

        return tasksFinalizadas == listTasks.size();
    }
}
