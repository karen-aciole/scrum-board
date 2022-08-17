package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.AdicionaIntegranteDTO;
import com.psoft.scrumboard.dto.ProjetoDTO;
import com.psoft.scrumboard.exception.ProjetoNotFoundException;
import com.psoft.scrumboard.exception.UsuarioAlreadyExistsException;
import com.psoft.scrumboard.exception.UsuarioNotAllowedException;
import com.psoft.scrumboard.exception.UsuarioNotFoundException;
import com.psoft.scrumboard.model.enums.PapelEnum;
import com.psoft.scrumboard.service.ProjetoService;
import com.psoft.scrumboard.service.UsuarioService;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;
    
    @Autowired
    private UsuarioService usuarioService;

    @RequestMapping(value = "/projeto/", method = RequestMethod.POST)
    public ResponseEntity<?> cadastraProjeto(@RequestBody ProjetoDTO projetoDTO)  {
        int projectname;

        try {
            projectname = this.projetoService.criaProjeto(projetoDTO);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não está cadastrado no sistema - username inválido", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>("Projeto cadastrado com chave = '" + projectname + "'", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/projeto/", method = RequestMethod.PUT)
    public ResponseEntity<?> adicionaIntegrante(
            @ApiParam(value = "", required = true, allowableValues = "DESENVOLVEDOR, ESTAGIARIO, PESQUISADOR, PRODUCT_OWNER")
            @RequestParam PapelEnum papel, @RequestParam String username, @RequestParam Integer projectKey, @RequestParam String scrumMaster) {

        try {
            AdicionaIntegranteDTO adicionaIntegranteDTO = new AdicionaIntegranteDTO(projectKey, papel, username, scrumMaster);
            this.projetoService.adicionaIntegrante(adicionaIntegranteDTO);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não cadastrado no sistema - projectname invalido", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não está cadastrado no sistema - username inválido", HttpStatus.CONFLICT);
        } catch (UsuarioAlreadyExistsException e) {
            return new ResponseEntity<String>("Usuário já está cadastrado no projeto", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Scrum Master não pertence a esse projeto", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>("Integrante cadastrado com name '" + username + "'", HttpStatus.CREATED);
    }


    @RequestMapping(value = "/projeto/{projectKey}", method = RequestMethod.GET)
    public ResponseEntity<?> acessaInfoProjeto(@PathVariable Integer projectKey) {
        String info;

        try {
            info = this.projetoService.getInfoProjeto(projectKey);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não cadastrado no sistema - projectKey inválido", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/projeto/{projectKey}/{scrumMaster}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeProjeto(@PathVariable Integer projectKey, @PathVariable String scrumMaster) {

        String info;

        try {
            info = this.projetoService.deletaProjeto(projectKey, scrumMaster);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não cadastrado no sistema - projectKey inválido", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Scrum Master informado não tem permissão para deletar este projeto.", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/projeto/{projectKey}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizaInfoProjeto(@PathVariable Integer projectKey, @RequestBody ProjetoDTO projetoDTO) {
        String info;

        try {
            info = this.projetoService.updateInfoProjeto(projectKey, projetoDTO);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não cadastrado no sistema - projectname invalido", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não está cadastrado no sistema - username inválido", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Scrum Master não pertence a esse projeto", HttpStatus.CONFLICT);
        }


        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

}
