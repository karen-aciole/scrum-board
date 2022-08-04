package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.ProjetoDTO;
import com.psoft.scrumboard.model.Integrante;
import com.psoft.scrumboard.model.Papel;
import com.psoft.scrumboard.model.Projeto;
import com.psoft.scrumboard.model.Usuario;
import com.psoft.scrumboard.repository.PapelRepository;
import com.psoft.scrumboard.repository.ProjetoRepository;
import com.psoft.scrumboard.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PapelRepository papelRepository;

    public String criaProjeto(String scrumMasterUsername, ProjetoDTO projetoDTO) {
        Usuario scrumMasterUsuario = usuarioRepository.getUser(scrumMasterUsername);
        Papel scrumMasterPapel = this.papelRepository.getPapelByID(0);
        Integrante scrumMaster = new Integrante(scrumMasterUsuario, scrumMasterPapel);
    	
    	Projeto projeto = new Projeto(projetoDTO.getNome(),
                projetoDTO.getDescricao(),
                projetoDTO.getInstituicaoParceira(), scrumMaster);

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
