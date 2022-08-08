package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.ProjetoDTO;
import com.psoft.scrumboard.service.ProjetoService;
import com.psoft.scrumboard.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;
    
    @Autowired
    private UsuarioService usuarioService;

    @RequestMapping(value = "/projeto/", method = RequestMethod.POST)
    public ResponseEntity<?> cadastraProjeto(@RequestBody ProjetoDTO projetoDTO, UriComponentsBuilder ucBuilder) {

        
        if (!(this.usuarioService.contemUsername(projetoDTO.getScrumMasterName()))) {
			return new ResponseEntity<String>("Usuário não está cadastrado no sistema - username inválido", HttpStatus.CONFLICT);
		}

        int projectname = this.projetoService.criaProjeto(projetoDTO);

        return new ResponseEntity<String>("Projeto cadastrado com chave = '" + projectname + "'", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/projeto/{projectKey}/{userName}", method = RequestMethod.PUT)
    public ResponseEntity<?> adicionaIntegrante(@RequestParam Integer projectKey, @RequestParam String userName, UriComponentsBuilder ucBuilder) {

        if (!this.projetoService.contemProjectKey(projectKey)) {
            return new ResponseEntity<String>("Projeto nao cadastrado no sistema - projectname invalido", HttpStatus.CONFLICT);
        }

        if (!(this.usuarioService.contemUsername(userName))) {
            return new ResponseEntity<String>("Usuário não está cadastrado no sistema - username inválido", HttpStatus.CONFLICT);
        }

        String projectname = this.projetoService.adicionaDesenvolvedor(projectKey, userName);

        return new ResponseEntity<String>("Integrante cadastrado com name '" + userName + "'", HttpStatus.CREATED);
    }


    @RequestMapping(value = "/projeto/{projectKey}", method = RequestMethod.GET)
    public ResponseEntity<?> acessaInfoProjeto(@PathVariable Integer projectKey) {


        if (!(this.projetoService.contemProjectKey(projectKey))) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - projectKey inválido", HttpStatus.CONFLICT);
        }

        String info = this.projetoService.getInfoProjeto(projectKey);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }


    @RequestMapping(value = "/projeto/{projectKey}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeProjeto(@PathVariable Integer projectKey) {

        if (!(this.projetoService.contemProjectKey(projectKey))) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - projectKey inválido", HttpStatus.CONFLICT);
        }

        String info = this.projetoService.deletaProjeto(projectKey);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/projeto/{projectKey}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizaInfoProjeto(@PathVariable Integer projectKey, @RequestBody ProjetoDTO projetoDTO, UriComponentsBuilder ucBuilder) {

        if (!(this.projetoService.contemProjectKey(projectKey))) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - projectKey inválido", HttpStatus.CONFLICT);
        }

        if (!(this.usuarioService.contemUsername(projetoDTO.getScrumMasterName()))) {
            return new ResponseEntity<String>("Usuário não está cadastrado no sistema - username inválido", HttpStatus.CONFLICT);
        }

        if (!this.projetoService.getScrumMasterName(projectKey).equals(projetoDTO.getScrumMasterName())) {
            return new ResponseEntity<String>("O Scrum master informado nao pertence ao projeto em questao", HttpStatus.CONFLICT);
        }


        String info = this.projetoService.updateInfoProjeto(projectKey, projetoDTO);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

}
