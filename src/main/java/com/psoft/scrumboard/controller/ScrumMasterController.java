package com.psoft.scrumboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class ScrumMasterController {

    @RequestMapping(value = "/scrummaster/papeisDisponiveis", method = RequestMethod.GET)
    public ResponseEntity<?> listaPapeis() {
        String papeis = "Papéis que podem ser assumidos por usuários associados a projetos:\n\n" +
                "- Product Owner\n" +
                "- Pesquisador\n" +
                "- Desenvolvedor\n" +
                "- Estagiário";

        return new ResponseEntity<String>(papeis, HttpStatus.OK);
    }
}
