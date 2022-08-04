package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.ProjetoDTO;
import com.psoft.scrumboard.service.ProjetoService;
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

    @RequestMapping(value = "/projeto/", method = RequestMethod.POST)
    public ResponseEntity<?> cadastraProjeto(@RequestBody ProjetoDTO projetoDTO, UriComponentsBuilder ucBuilder) {

        if (this.projetoService.contemProjectname(projetoDTO.getNome())) {
            return new ResponseEntity<String>("Projeto já cadastrado no sistema - projectname não disponível", HttpStatus.CONFLICT);
        }

        String projectname = this.projetoService.criaProjeto(projetoDTO);

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
