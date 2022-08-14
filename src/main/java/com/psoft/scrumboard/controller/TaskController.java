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

    @RequestMapping(value = "/task/", method = RequestMethod.POST)
    public ResponseEntity<?> cadastraTask(@RequestBody TaskDTO taskDTO) {
        int id;

        try {
            id = this.taskService.criaTask(taskDTO);
        } catch (TaskAlreadyExistsException | UserStoryNotFoundException | UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Erro ao criar a task", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>("Task cadastrada com o '" + id + "'", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/task/{taskId}/{idUserStory}/{userName}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeTask(@PathVariable Integer taskId, @PathVariable Integer idUserStory, @PathVariable String userName) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {
        String info = this.taskService.deletaTask(taskId, idUserStory, userName);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/task/{taskId}/{idUserStory}/{userName}", method = RequestMethod.GET)
    public ResponseEntity<?> acessaInfoTask(@PathVariable Integer taskId, @PathVariable Integer idUserStory, @PathVariable String userName) throws TaskNotFoundException, UserStoryNotFoundException, UsuarioNotAllowedException {

        String info = this.taskService.getInfoTask(taskId, idUserStory, userName);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }


    @RequestMapping(value = "/task/{taskId}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizaInfoTask(@PathVariable  Integer taskId, @RequestBody TaskDTO taskDTO) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {

        String info = this.taskService.updateInfoTask(taskId, taskDTO);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/task/{taskId}/{idUserStory}/{userName}", method = RequestMethod.PUT)
    public ResponseEntity<?> mudaStatusTask(@PathVariable Integer taskId, @PathVariable Integer idUserStory, @PathVariable String userName) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {

        String info = this.taskService.mudaStatusTask(taskId, idUserStory, userName);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

}
