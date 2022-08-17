package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.MudaStatusTaskDTO;
import com.psoft.scrumboard.dto.TaskDTO;
import com.psoft.scrumboard.exception.*;
import com.psoft.scrumboard.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class TaskController {
    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/task/{projectKey}", method = RequestMethod.POST)
    public ResponseEntity<?> cadastraTask(@PathVariable Integer projectKey, @RequestBody TaskDTO taskDTO) {
        int id;

        try {
            id = this.taskService.criaTask(projectKey, taskDTO);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<>("Projeto não encontrado", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<>("US não está cadastrada no sistema - id inválido.", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<>("Usuário especificado não pode realizar essa operação", HttpStatus.FORBIDDEN);
        } catch (TaskAlreadyExistsException e) {
            return new ResponseEntity<>("Tarefa já cadastrada no sistema.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>("Task cadastrada com o '" + id + "'", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/task/{taskId}/{idUserStory}/{projectKey}/", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeTask(@PathVariable Integer taskId, @PathVariable Integer idUserStory, @PathVariable Integer projectKey, @RequestParam String username) {
        String info;
        try {
            info = this.taskService.deletaTask(taskId, idUserStory, username, projectKey);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não existe", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("US não está cadastrada no sistema - id inválido", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Usuário especificado não pode realizar essa operação", HttpStatus.FORBIDDEN);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<String>("Task não encontrada", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/task/{taskId}/{idUserStory}/{projectKey}/", method = RequestMethod.GET)
    public ResponseEntity<?> acessaInfoTask(@PathVariable Integer taskId, @PathVariable Integer idUserStory, @PathVariable Integer projectKey, @PathVariable String userName) {

        String info;
        try {
            info = this.taskService.getInfoTask(taskId, idUserStory, userName, projectKey);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não existe", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("US não está cadastrada no sistema - id inválido", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Usuário especificado não pode realizar essa operação", HttpStatus.FORBIDDEN);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<String>("Task não encontrada", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }


    @RequestMapping(value = "/task/{taskId}/{projectKey}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizaInfoTask(@PathVariable Integer taskId, @PathVariable Integer projectKey, @RequestBody TaskDTO taskDTO) {

        String info;
        try {
            info = this.taskService.updateInfoTask(taskId, projectKey, taskDTO);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não existe", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("US não está cadastrada no sistema - id inválido", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Usuário especificado não pode realizar essa operação", HttpStatus.FORBIDDEN);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<String>("Task não encontrada", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/task/{taskId}/{idUserStory}/{projectKey}/", method = RequestMethod.PUT)
    public ResponseEntity<?> mudaStatusTask(@PathVariable Integer taskId, @PathVariable Integer idUserStory, @PathVariable Integer projectKey, @RequestParam String username){
        String info;
        try {
            MudaStatusTaskDTO mudaStatusTaskDTO = new MudaStatusTaskDTO(taskId, idUserStory, username);
            info = this.taskService.mudaStatusTask(mudaStatusTaskDTO, projectKey);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não existe", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("US não está cadastrada no sistema - id inválido", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException | UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário especificado não pode realizar essa operação", HttpStatus.FORBIDDEN);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<String>("Task não encontrada", HttpStatus.CONFLICT);
        } catch (StatusException e) {
            return new ResponseEntity<String>("Não é possível finalizar task. Pois não há nenhum usuário atribuído a ela", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

}
