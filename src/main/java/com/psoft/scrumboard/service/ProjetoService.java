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

    public int criaProjeto(ProjetoDTO projetoDTO) {
        Usuario scrumMasterUsuario = usuarioRepository.getUser(projetoDTO.getScrumMasterName());
        Papel scrumMasterPapel = this.papelRepository.getPapelByID(0);
        Integrante scrumMaster = new Integrante(scrumMasterUsuario, scrumMasterPapel);
    	
    	Projeto projeto = new Projeto(projetoDTO.getNome(),
                projetoDTO.getDescricao(),
                projetoDTO.getInstituicaoParceira(), scrumMaster);

        return this.projetoRepository.addProjeto(projeto);
    }

    public boolean contemProjectname(String projectName) {
        return this.projetoRepository.containsProjectname(projectName);
    }


    public String adicionaDesenvolvedor(Integer projectKey, String desenvolvedorUsername){
        Usuario desenvolvedorUsuario = usuarioRepository.getUser(desenvolvedorUsername);
        Papel desenvolvedorPapel = this.papelRepository.getPapelByID(3);
        Integrante desenvolvedor = new Integrante(desenvolvedorUsuario, desenvolvedorPapel);

        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        projeto.adicionaIntegrante(desenvolvedor);

        return projeto.getNome();
    }

    public boolean contemProjectKey(Integer projectKey) {
        return this.projetoRepository.containsProjectKey(projectKey);

    }

    public String getInfoProjeto(Integer projectKey) {
        Projeto projeto = this.projetoRepository.getProjeto(projectKey);

        return projeto.toString();

    }

    public String getScrumMasterName(Integer projectKey){
        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        Integrante integrante = projeto.getScrumMaster();
        String scrumMastername = integrante.getUsuario().getUsername();

        return scrumMastername;
    }

    public String deletaProjeto(Integer projectKey) {
        this.projetoRepository.delProject(projectKey);

        return "Projeto removido com nome '" + projectKey + "'";
    }
    
    public boolean contemIntegrante(Integer nomeProjeto, String username) {
    	return this.projetoRepository.getProjeto(nomeProjeto).contemIntegrante(username);
    }

    public String updateInfoProjeto(Integer key, ProjetoDTO projetoDTO) {
        Projeto projeto = this.projetoRepository.getProjeto(key);

        projeto.setDescricao(projetoDTO.getDescricao());
        projeto.setName(projetoDTO.getNome());
        projeto.setInstituicaoParceira(projetoDTO.getInstituicaoParceira());

        return "Projeto atualizado com nome: '" + projeto.getNome() + "',\ndescricao: '" + projeto.getDescricao() + "'\n" +
                "Instituicao parceira: '" + projeto.getInstituicaoParceira() + "'\nScrum Master: " + projetoDTO.getScrumMasterName();

    }
}
