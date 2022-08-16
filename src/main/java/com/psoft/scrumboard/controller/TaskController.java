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
        } catch (TaskAlreadyExistsException | UserStoryNotFoundException | UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Erro ao criar a task", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>("Task cadastrada com o '" + id + "'", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/task/{taskId}/{idUserStory}/{projectKey}/{userName}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeTask(@PathVariable Integer taskId, @PathVariable Integer idUserStory, @PathVariable Integer projectKey, @PathVariable String userName) {
        String info;
        try {
            info = this.taskService.deletaTask(taskId, idUserStory, userName, projectKey);
        } catch (UserStoryNotFoundException | TaskNotFoundException | UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Erro ao deletar a task", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/task/{taskId}/{idUserStory}/{projectKey}/{userName}", method = RequestMethod.GET)
    public ResponseEntity<?> acessaInfoTask(@PathVariable Integer taskId, @PathVariable Integer idUserStory, @PathVariable Integer projectKey, @PathVariable String userName) {

        String info;
        try {
            info = this.taskService.getInfoTask(taskId, idUserStory, userName, projectKey);
        } catch (TaskNotFoundException | UserStoryNotFoundException e) {
            return new ResponseEntity<String>("Erro ao acessar a task", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Erro ao acessar a task", HttpStatus.FORBIDDEN);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não existe", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }


    @RequestMapping(value = "/task/{taskId}/{projectKey}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizaInfoTask(@PathVariable Integer taskId, @PathVariable Integer projectKey, @RequestBody TaskDTO taskDTO) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {

        String info = this.taskService.updateInfoTask(taskId, projectKey, taskDTO);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/task/{taskId}/{idUserStory}/{projectKey}/{userName}", method = RequestMethod.PUT)
    public ResponseEntity<?> mudaStatusTask(@PathVariable Integer taskId, @PathVariable Integer idUserStory, @PathVariable Integer projectKey, @PathVariable String userName) {

        String info;
        try {
            info = this.taskService.mudaStatusTask(taskId, idUserStory, userName, projectKey);
        } catch (TaskNotFoundException | UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Erro ao muda status da task", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

}
