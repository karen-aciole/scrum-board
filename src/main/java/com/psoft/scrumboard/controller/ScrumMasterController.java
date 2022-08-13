package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.service.ScrumMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class ScrumMasterController {
	
	@Autowired
	ScrumMasterService scrumMasterService;
	
	@RequestMapping(value = "/scrummaster/papeis-disponiveis", method = RequestMethod.GET)
    public ResponseEntity<?> listaPapeis() {
		
		String papeis = this.scrumMasterService.getPapeis();
		
		return new ResponseEntity<String>(papeis, HttpStatus.OK);
    }
    @RequestMapping(value = "/scrummaster/estagios-desenvolvimento-disponiveis", method = RequestMethod.GET)
    public ResponseEntity<?> listaEstagiosDesenvolvimento() {
        
    	String estagiosDesenvolvimento = this.scrumMasterService.getEstagiosDesenvolvimento();

        return new ResponseEntity<String>(estagiosDesenvolvimento, HttpStatus.OK);
    }

}
