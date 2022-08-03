package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.ProjetoDTO;
import com.psoft.scrumboard.model.Projeto;
import com.psoft.scrumboard.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    public String criaProjeto(ProjetoDTO projetoDTO) {
        Projeto projeto = new Projeto(projetoDTO.getNome(),
                projetoDTO.getDescricao(),
                projetoDTO.getInstituicaoParceira());

        this.projetoRepository.addProjeto(projeto);

        return projeto.getNome();
    }

    public boolean contemProjectname(String projectname) {
        return this.projetoRepository.containsProjectname(projectname);
    }

    public String getInfoProjeto(String projectname) {
        Projeto projeto = this.projetoRepository.getProjeto(projectname);

        return projeto.toString();

    }

    public String deletaProjeto(String projectname) {
        this.projetoRepository.delProject(projectname);

        return "Projeto removido com nome '" + projectname + "'";
    }

}
