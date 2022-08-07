package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.ProjetoDTO;
import com.psoft.scrumboard.model.Integrante;
import com.psoft.scrumboard.model.Projeto;
import com.psoft.scrumboard.model.Usuario;
import com.psoft.scrumboard.model.papel.Papel;
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

    public String getScrumMasterName(String projectname){
        Projeto projeto = this.projetoRepository.getProjeto(projectname);
        Integrante integrante = projeto.getScrumMaster();
        String scrumMastername = integrante.getUsuario().getUsername();

        return scrumMastername;
    }

    public String deletaProjeto(String projectname) {
        this.projetoRepository.delProject(projectname);

        return "Projeto removido com nome '" + projectname + "'";
    }
    
    public boolean contemIntegrante(String nomeProjeto, String username) {
    	return this.projetoRepository.getProjeto(nomeProjeto).contemIntegrante(username);
    }

    public String updateInfoProjeto(String scrumMaster, ProjetoDTO projetoDTO) {
        Projeto projeto = this.projetoRepository.getProjeto(projetoDTO.getNome());

        Usuario scrumMasterUsuario = usuarioRepository.getUser(scrumMaster);
        Papel scrumMasterPapel = this.papelRepository.getPapelByID(0);
        Integrante scrumMasterIntegrante = new Integrante(scrumMasterUsuario, scrumMasterPapel);

        projeto = new Projeto(projetoDTO.getNome(),
                projetoDTO.getDescricao(),
                projetoDTO.getInstituicaoParceira(),
                scrumMasterIntegrante);

        this.projetoRepository.addProjeto(projeto);

        return "Projeto atualizado com nome: '" + projeto.getNome() + "',\ndescricao: '" + projeto.getDescricao() + "'\n" +
                "Instituicao parceira: '" + projeto.getInstituicaoParceira() + "'\nScrum Maste: " + projeto.getScrumMaster();

    }
}
