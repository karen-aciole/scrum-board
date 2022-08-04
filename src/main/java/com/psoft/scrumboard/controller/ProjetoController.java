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
    public ResponseEntity<?> cadastraProjeto(@RequestParam String scrumMasterUsername, @RequestBody ProjetoDTO projetoDTO, UriComponentsBuilder ucBuilder) {

        if (this.projetoService.contemProjectname(projetoDTO.getNome())) {
            return new ResponseEntity<String>("Projeto já cadastrado no sistema - projectname não disponível", HttpStatus.CONFLICT);
        }
        
        if (!(this.usuarioService.contemUsername(scrumMasterUsername))) {
			return new ResponseEntity<String>("Usuário não está cadastrado no sistema - username inválido", HttpStatus.CONFLICT);
		}

        String projectname = this.projetoService.criaProjeto(scrumMasterUsername, projetoDTO);

        return new ResponseEntity<String>("Projeto cadastrado com projectname '" + projetoDTO.getNome() + "'", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/projeto/{projectname}", method = RequestMethod.GET)
    public ResponseEntity<?> acessaInfoProjeto(@PathVariable String projectname) {

        if (!(this.projetoService.contemProjectname(projectname))) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - projectname inválido", HttpStatus.CONFLICT);
        }

        String info = this.projetoService.getInfoProjeto(projectname);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }


    @RequestMapping(value = "/projeto/{projectname}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeProjeto(@PathVariable String projectname) {

        if (!(this.projetoService.contemProjectname(projectname))) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - projectname inválido", HttpStatus.CONFLICT);
        }

        String info = this.projetoService.deletaProjeto(projectname);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }
}
