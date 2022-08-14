package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.MudaStatusTaskDTO;
import com.psoft.scrumboard.dto.TaskDTO;
import com.psoft.scrumboard.exception.TaskAlreadyExistsException;
import com.psoft.scrumboard.exception.TaskNotFoundException;
import com.psoft.scrumboard.exception.UserStoryNotFoundException;
import com.psoft.scrumboard.exception.UsuarioNotAllowedException;
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
        } catch (TaskAlreadyExistsException e) {
            return new ResponseEntity<String>("Task já cadastrado no sistema - titulo não disponível", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>("Task cadastrada com o '" + id + "'", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/task/", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeTask(@RequestBody Integer userStoryID, Integer taskID) throws UserStoryNotFoundException {

        String info = this.taskService.deletaTask(userStoryID, taskID);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/task/", method = RequestMethod.GET)
    public ResponseEntity<?> acessaInfoTask(@RequestParam Integer userStoryID, Integer taskID) throws UserStoryNotFoundException {
        String info = this.taskService.getInfoTask(userStoryID, taskID);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/task/{taskId}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizaInfoTask(@PathVariable  Integer taskId, @RequestBody TaskDTO taskDTO) throws UserStoryNotFoundException {

        String info = this.taskService.updateInfoTask(taskId, taskDTO);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/task/", method = RequestMethod.PUT)
    public ResponseEntity<?> mudaStatusTask(@RequestBody MudaStatusTaskDTO mudaStatusTask) throws UserStoryNotFoundException, TaskNotFoundException, UsuarioNotAllowedException {

        String info = this.taskService.mudaStatusTask(mudaStatusTask);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

}
