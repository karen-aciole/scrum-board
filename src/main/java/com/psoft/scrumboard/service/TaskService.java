package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.MudaStatusDTO;
import com.psoft.scrumboard.dto.MudaStatusTaskDTO;
import com.psoft.scrumboard.dto.TaskDTO;
import com.psoft.scrumboard.exception.*;
import com.psoft.scrumboard.model.Projeto;
import com.psoft.scrumboard.model.Task;
import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.model.event.UserStoryEvent;
import com.psoft.scrumboard.repository.observer.UserStorySource;
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
    private UserStorySource userStorySource;
    
    @Autowired
    private ProjetoRepository projetoRepository;
    
    @Autowired
    private UserStoryRepository userStoryRepository;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private EstagioDesenvolvimentoRepository estagioDesenvolvimentoRepository;
    
    public int criaTask(Integer projectKey, TaskDTO taskDTO) throws TaskAlreadyExistsException, UserStoryNotFoundException, UsuarioNotAllowedException, ProjetoNotFoundException {

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não encontrado");
        }

        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        UserStory userStory = projeto.getUserStory(taskDTO.getUserStoryID());

        if (userStory == null) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        } else if (!userStory.getResponsaveis().containsUsername(taskDTO.getUserName()) && !projeto.getScrumMaster().getUsuario().getUsername().equals(taskDTO.getUserName())) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        }

        Task task = new Task(taskDTO.getTitulo(), taskDTO.getDescricao(), taskDTO.getUserStoryID());

        return userStory.getTasks().addTask(task);
    }


    public String deletaTask(Integer taskId, Integer idUserStory, String userName, Integer projectKey) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException, ProjetoNotFoundException {
        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não encontrado");
        }

        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        UserStory userStory = projeto.getUserStory(idUserStory);

        if (userStory == null) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        } else if (!userStory.getResponsaveis().containsUsername(userName) && !projeto.getScrumMaster().getUsuario().getUsername().equals(userName)) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        } else if (!userStory.getTasks().containsTask(taskId)) {
            throw new TaskNotFoundException("Tarefa não encontrada");
        }

        userStory.getTasks().delTask(taskId);

        return "Task deletada com sucesso";
    }

    public String getInfoTask(Integer taskId, Integer idUserStory, String userName, Integer projectKey)
            throws TaskNotFoundException, UserStoryNotFoundException, UsuarioNotAllowedException, ProjetoNotFoundException {

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não encontrado");
        }

        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        UserStory userStory = projeto.getUserStory(idUserStory);

        if (userStory == null) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        } else if (!userStory.getResponsaveis().containsUsername(userName) && !projeto.getScrumMaster().getUsuario().getUsername().equals(userName)) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        } else if (!userStory.getTasks().containsTask(taskId)) {
            throw new TaskNotFoundException("Tarefa não encontrada");
        }

        return userStory.getTasks().getTask(taskId).toString();
    }

    public String updateInfoTask(Integer taskId, Integer projectKey, TaskDTO taskDTO) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException, ProjetoNotFoundException {
        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não encontrado");
        }

        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        UserStory userStory = projeto.getUserStory(taskDTO.getUserStoryID());

        if (userStory == null) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        } else if (!userStory.getResponsaveis().containsUsername(taskDTO.getUserName()) && !projeto.getScrumMaster().getUsuario().getUsername().equals(taskDTO.getUserName())) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        } else if (!userStory.getTasks().containsTask(taskId)) {
            throw new TaskNotFoundException("Tarefa não encontrada");
        }

        Task task = userStory.getTasks().getTask(taskId);

        task.setDescricao(!taskDTO.getDescricao().isBlank() ? taskDTO.getDescricao() : task.getDescricao());
        task.setTitulo(!taskDTO.getTitulo().isBlank() ? taskDTO.getTitulo() : task.getTitulo());

        return userStory.getTasks().getTask(taskId).toString();
    }

    public String mudaStatusTask(MudaStatusTaskDTO mudaStatusTaskDTO, Integer projetoKey)
            throws TaskNotFoundException, UsuarioNotAllowedException, ProjetoNotFoundException, StatusException, UserStoryNotFoundException, UsuarioNotFoundException {

        if (!this.projetoRepository.containsProjectKey(projetoKey)) {
            throw new ProjetoNotFoundException("Projeto não encontrado");
        }

        Projeto projeto = this.projetoRepository.getProjeto(projetoKey);
        UserStory userStory = projeto.getUserStory(mudaStatusTaskDTO.getIdUserStory());

        if (userStory == null) {
            throw new UserStoryNotFoundException("US não está cadastrada no sistema - id inválido.");
        } else if (!userStory.getResponsaveis().containsUsername(mudaStatusTaskDTO.getUsername()) && !projeto.getScrumMaster().getUsuario().getUsername().equals(mudaStatusTaskDTO.getUsername())) {
            throw new UsuarioNotAllowedException("Usuário especificado não pode realizar essa operação");
        } else if (!userStory.getTasks().containsTask(mudaStatusTaskDTO.getTaskKey())) {
            throw new TaskNotFoundException("Tarefa não encontrada");
        } else if (userStory.getEstagioDesenvolvimento().equals(EstagioDesenvolvimentoEnum.TO_DO)) {
            throw new StatusException ("Não é possível finalizar task. Pois não há nenhum usuário atribuído a ela");
        }

        Task task = userStory.getTasks().getTask(mudaStatusTaskDTO.getTaskKey());

        task.setStatus();
        this.userStorySource.marcouTaskRealizada(mudaStatusTaskDTO.getTaskKey(), task.getStatus());

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
