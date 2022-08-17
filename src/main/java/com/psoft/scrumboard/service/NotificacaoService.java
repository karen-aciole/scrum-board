package com.psoft.scrumboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psoft.scrumboard.dto.MudaStatusDTO;
import com.psoft.scrumboard.exception.ProjetoNotFoundException;
import com.psoft.scrumboard.exception.UserStoryNotFoundException;
import com.psoft.scrumboard.exception.UsuarioNotAllowedException;
import com.psoft.scrumboard.exception.UsuarioNotFoundException;
import com.psoft.scrumboard.model.Projeto;
import com.psoft.scrumboard.model.enums.PapelEnum;
import com.psoft.scrumboard.model.listener.ProductOwnerListener;
import com.psoft.scrumboard.model.listener.ScrumMasterListener;
import com.psoft.scrumboard.model.listener.UserStoryListener;
import com.psoft.scrumboard.model.listener.UsuarioListener;
import com.psoft.scrumboard.repository.ProjetoRepository;
import com.psoft.scrumboard.repository.observer.UserStorySource;

@Service
public class NotificacaoService {
	
	@Autowired
    private UserStorySource userStorySource;
	
	@Autowired
    private ProjetoRepository projetoRepository;
	
	@Autowired
    private UserStoryService userStoryService;
	
	public String addInscricaoUsuario(MudaStatusDTO inscricaoDTO)
    		throws ProjetoNotFoundException, UserStoryNotFoundException, UsuarioNotFoundException {
    	
    	if (!this.projetoRepository.containsProjectKey(inscricaoDTO.getProjectKey())) {
    		throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
    	}
    	
    	Projeto projeto = this.projetoRepository.getProjeto(inscricaoDTO.getProjectKey());
    	
    	if (!this.userStoryService.contemUserStory(inscricaoDTO.getProjectKey(), inscricaoDTO.getIdUserStory())) {
    		throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");
    	}
    	
    	if (!(projeto.contemIntegrante(inscricaoDTO.getUsername()))) {
            throw new UsuarioNotFoundException("Usuário não é integrante do projeto.");
        }
        
        UserStoryListener usuario = new UsuarioListener(inscricaoDTO.getUsername());
        this.userStorySource.addListener(usuario);
    	
        return "Inscrição realizada!";
    }
    
    
    public String addInscricaoScrumMaster(MudaStatusDTO inscricaoDTO)
    		throws ProjetoNotFoundException, UsuarioNotAllowedException {
    	
    	if (!this.projetoRepository.containsProjectKey(inscricaoDTO.getProjectKey())) {
    		throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
    	}
    	
    	Projeto projeto = this.projetoRepository.getProjeto(inscricaoDTO.getProjectKey());
    	
    	if (!(projeto.getScrumMaster().getUsuario().getUsername().equals(inscricaoDTO.getUsername()))) {
    		throw new UsuarioNotAllowedException("O username informado não é do Scrum Master desse projeto.");
        }
        
        UserStoryListener usuario = new ScrumMasterListener(inscricaoDTO.getUsername());
        this.userStorySource.addListener(usuario);
    	
        return "Inscrição realizada!";
    }
    
    public String addInscricaoProductOwner(MudaStatusDTO inscricaoDTO)
    		throws ProjetoNotFoundException, UsuarioNotFoundException {
    	
    	if (!this.projetoRepository.containsProjectKey(inscricaoDTO.getProjectKey())) {
    		throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
    	}
    	
    	Projeto projeto = this.projetoRepository.getProjeto(inscricaoDTO.getProjectKey());
    	
    	if (!(projeto.contemIntegrante(inscricaoDTO.getUsername())
    			|| projeto.getIntegranteRepository().getIntegrante(inscricaoDTO.getUsername()).getPapel().getTipo().equals(PapelEnum.PRODUCT_OWNER))) {
    		throw new UsuarioNotFoundException("Usuário não é Product Owner neste projeto.");
        }
        
        UserStoryListener usuario = new ProductOwnerListener(inscricaoDTO.getUsername());
        this.userStorySource.addListener(usuario);
    	
        return "Inscrição realizada!";
    }

}
