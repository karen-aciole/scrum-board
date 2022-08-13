package com.psoft.scrumboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psoft.scrumboard.model.estagiodesenvolvimento.EstagioDesenvolvimento;
import com.psoft.scrumboard.model.papel.Papel;
import com.psoft.scrumboard.repository.EstagioDesenvolvimentoRepository;
import com.psoft.scrumboard.repository.PapelRepository;

@Service
public class ScrumMasterService {
	
	@Autowired
	private PapelRepository papelRepository;
	
	@Autowired
	private EstagioDesenvolvimentoRepository estagioDesenvolvimentoRepository;
	
	public String getPapeis() {
		String infoPapeis = "Papéis que podem ser assumidos por usuários associados a projetos:\n\n";
		
		for (Papel p : this.papelRepository.getPapeis()) {
			infoPapeis += "- " + p.getTipo().name() + "\n";
		}
		
		return infoPapeis;
	}
	
	public String getEstagiosDesenvolvimento() {
		String infoEstagiosDesenvolvimento = "Estágios de desenvolvimento que uma User Story pode assumir:\n\n";
		
		for (EstagioDesenvolvimento e : this.estagioDesenvolvimentoRepository.getEstagiosDesenvolvimento()) {
			infoEstagiosDesenvolvimento += "- " + e.getTipo().name() + "\n";
		}
		
		return infoEstagiosDesenvolvimento; 
	}
	
}
