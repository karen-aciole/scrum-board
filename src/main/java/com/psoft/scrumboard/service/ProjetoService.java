package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.AdicionaIntegranteDTO;
import com.psoft.scrumboard.dto.ProjetoDTO;
import com.psoft.scrumboard.exception.*;
import com.psoft.scrumboard.model.Integrante;
import com.psoft.scrumboard.model.Projeto;
import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.model.Usuario;
import com.psoft.scrumboard.model.papel.Papel;
import com.psoft.scrumboard.model.enums.PapelEnum;
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

    public int criaProjeto(ProjetoDTO projetoDTO) throws UsuarioNotFoundException {
        Usuario scrumMasterUsuario = usuarioRepository.getUser(projetoDTO.getScrumMasterName());

        if (scrumMasterUsuario == null)
            throw new UsuarioNotFoundException("Usuário não está cadastrado no sistema - username inválido");


        Papel scrumMasterPapel = this.papelRepository.getPapelByEnum(PapelEnum.SCRUM_MASTER);
        Integrante scrumMaster = new Integrante(scrumMasterUsuario, scrumMasterPapel);
    	
    	Projeto projeto = new Projeto(projetoDTO.getNome(),
                projetoDTO.getDescricao(),
                projetoDTO.getInstituicaoParceira(), scrumMaster);

        Integer projectKey = this.projetoRepository.addProjeto(projeto);
        this.adicionaScrumMasterComoIntegrante(projetoDTO.getScrumMasterName(), projectKey);

        return projectKey;
    }

    private void adicionaScrumMasterComoIntegrante(String scrumMaster, Integer projectKey) {
        Usuario integranteSM = usuarioRepository.getUser(scrumMaster);
        Papel papelSM = this.papelRepository.getPapelByEnum(PapelEnum.SCRUM_MASTER);
        Integrante ScrumMaster = new Integrante(integranteSM, papelSM);

        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        projeto.adicionaIntegrante(ScrumMaster);
    }

    public void adicionaIntegrante(AdicionaIntegranteDTO adicionaIntegranteDTO)
            throws ProjetoNotFoundException, UsuarioAlreadyExistsException, UsuarioNotAllowedException, UsuarioNotFoundException {

        Integer projectKey = adicionaIntegranteDTO.getProjectKey();
        String username = adicionaIntegranteDTO.getUserName();
        String scrumMaster = adicionaIntegranteDTO.getScrumMasterName();

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!this.usuarioRepository.containsUsername(username)) {
            throw new UsuarioNotFoundException("Usuário não está cadastrado no sistema - username inválido.");
        } else if (!(this.getScrumMasterName(projectKey).equals(scrumMaster))) {
            throw new UsuarioNotAllowedException("O Scrum Master informado não possui autorização para adicionar integrantes neste projeto.");
        } else if (this.contemIntegrante(projectKey, username)) {
            throw new UsuarioAlreadyExistsException("Usuário já é integrante deste projeto");
        }

        Usuario integranteUsuario = usuarioRepository.getUser(username);
        Papel integrantePapel = this.papelRepository.getPapelByEnum(adicionaIntegranteDTO.getPapel());
        Integrante integrante = new Integrante(integranteUsuario, integrantePapel);

        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        projeto.adicionaIntegrante(integrante);

    }

    public String getInfoProjeto(Integer projectKey) throws ProjetoNotFoundException {
        if (!this.projetoRepository.containsProjectKey(projectKey))
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");

        Projeto projeto = this.projetoRepository.getProjeto(projectKey);

        return projeto.toString();
    }

    public String getScrumMasterName(Integer projectKey){
        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        Integrante integrante = projeto.getScrumMaster();
        String scrumMastername = integrante.getUsuario().getUsername();

        return scrumMastername;
    }

    public String deletaProjeto(Integer projectKey, String scrumMasterUsername) throws ProjetoNotFoundException, UsuarioNotAllowedException {
        String scrumMaster = this.getScrumMasterName(projectKey);

        if (!this.projetoRepository.containsProjectKey(projectKey))
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        else if (!scrumMaster.equals(scrumMasterUsername)) {
            throw new UsuarioNotAllowedException("O Scrum Master informado não possui autorização para deletar este projeto.");
        }

        this.projetoRepository.delProject(projectKey);

        return "Projeto removido com key '" + projectKey + "'";
    }
    
    public boolean contemIntegrante(Integer projectKey, String username) {
    	return this.projetoRepository.getProjeto(projectKey).contemIntegrante(username);
    }

    public Integrante getIntegranteByUserName(Integer id, String userName){
        return this.projetoRepository.getProjeto(id).getIntegranteRepository().getIntegrante(userName);
    }

    public String updateInfoProjeto(Integer key, ProjetoDTO projetoDTO)
            throws ProjetoNotFoundException, UsuarioNotFoundException, UsuarioNotAllowedException {

        if (!this.projetoRepository.containsProjectKey(key)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!this.usuarioRepository.containsUsername(projetoDTO.getScrumMasterName())) {
            throw new UsuarioNotFoundException("Usuário não está cadastrado no sistema - username inválido.");
        } else if (!(this.getScrumMasterName(key).equals(projetoDTO.getScrumMasterName()))) {
            throw new UsuarioNotAllowedException("O Scrum Master informado não possui autorização para adicionar integrantes neste projeto.");
        }

        Projeto projeto = this.projetoRepository.getProjeto(key);

        projeto.setDescricao(!projetoDTO.getDescricao().isBlank() ? projetoDTO.getDescricao() : projeto.getDescricao());
        projeto.setName(!projetoDTO.getNome().isBlank() ? projetoDTO.getNome() : projeto.getNome());
        projeto.setInstituicaoParceira(!projetoDTO.getInstituicaoParceira().isBlank() ? projetoDTO.getInstituicaoParceira() : projeto.getInstituicaoParceira());

        return "Projeto atualizado com nome: '" + projeto.getNome() + "',\ndescricao: '" + projeto.getDescricao() + "'\n" +
                "Instituicao parceira: '" + projeto.getInstituicaoParceira() + "'\nScrum Master: " + projetoDTO.getScrumMasterName();

    }

    public Projeto encontraProjetoPorIDUserStory(Integer id){
        for(Projeto projeto: this.projetoRepository.getProjetos()){
            if(projeto.getUserStoryRepository().containsUserStory(id)){
                return projeto;
            }
        }

        return null;
    }
}
