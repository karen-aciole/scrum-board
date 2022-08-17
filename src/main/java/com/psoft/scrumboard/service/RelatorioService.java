package com.psoft.scrumboard.service;

import com.psoft.scrumboard.exception.ProjetoNotFoundException;
import com.psoft.scrumboard.exception.UserStoryNotFoundException;
import com.psoft.scrumboard.exception.UsuarioNotAllowedException;
import com.psoft.scrumboard.exception.UsuarioNotFoundException;
import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.model.enums.EstagioDesenvolvimentoEnum;
import com.psoft.scrumboard.model.enums.PapelEnum;
import com.psoft.scrumboard.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class RelatorioService {
    @Autowired
    private ProjetoRepository projetoRepository;
    @Autowired
    private ProjetoService projetoService;

    public String listaRelatorioDeUsersStoriesDeUmUsuario(Integer projectKey, String username) throws ProjetoNotFoundException, UsuarioNotFoundException {

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!(this.projetoService.contemIntegrante(projectKey, username))) {
            throw new UsuarioNotFoundException("Usuário não é integrante deste projeto");
        }

        int userStoriesTotal = getTotalDeUserStoriesByProject(projectKey);
        int userStoriesByUsuario = getTotalDeUserStoriesByIntegrante(projectKey, username);
        float percentualUserStoriesByUsuario = ((float) userStoriesByUsuario / userStoriesTotal) * 100;
        String percentualUserStoriesByUsuarioFormatado = String.format("%.2f", percentualUserStoriesByUsuario);

        int totalUserStoriesWorkInProgressByUsuario = getTotalUserStoriesFromUserByStatusWorkInProgress(projectKey, username);
        int totalUserStoriesToVerifyByUsuario = getTotalUserStoriesFromUserByStatusToVerify(projectKey, username);
        int totalUserStoriesDoneByUsuario = getTotalUserStoriesFromUserByStatusDone(projectKey, username);

        String percentualUserStoriesWorkInProgress = String.format("%.2f", ((float) totalUserStoriesWorkInProgressByUsuario / userStoriesByUsuario) * 100);
        String percentualUserStoriesToVerify = String.format("%.2f", ((float) totalUserStoriesToVerifyByUsuario / userStoriesByUsuario) * 100);
        String percentualUserStoriesDone = String.format("%.2f", ((float) totalUserStoriesDoneByUsuario / userStoriesByUsuario) * 100);


        if (percentualUserStoriesByUsuario == 0) return "Não há User Stories atribuídas para o usuário: " + username;

        return "Percentual de User Stories do usuário " + username + ": " + percentualUserStoriesByUsuarioFormatado + "%\n" + "Total de User Stories atribuídas ao usuário: " + userStoriesByUsuario + "/" + userStoriesTotal + "\n" + "Percentual de User Stories do usuário em cada estágio de desenvolvimento: \n" + "Work In Progress: " + percentualUserStoriesWorkInProgress + "% esse percentual representa um total de: " + totalUserStoriesWorkInProgressByUsuario + " User Storys\n" + "To Verify: " + percentualUserStoriesToVerify + "% esse percentual representa um total de: " + totalUserStoriesToVerifyByUsuario + " User Storys\n" + "Done: " + percentualUserStoriesDone + "% esse percentual representa um total de: " + totalUserStoriesDoneByUsuario + " User Storys\n";
    }

    private int getTotalUserStoriesFromUserByStatusWorkInProgress(Integer projectKey, String username) {
        return listAllUserStoriesFromUserByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.WORK_IN_PROGRESS, username).size();
    }

    private int getTotalUserStoriesFromUserByStatusToVerify(Integer projectKey, String username) {
        return listAllUserStoriesFromUserByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.TO_VERIFY, username).size();
    }

    private int getTotalUserStoriesFromUserByStatusDone(Integer projectKey, String username) {
        return listAllUserStoriesFromUserByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.DONE, username).size();
    }

    private Collection<UserStory> listAllUserStoriesByEstagioDesenvolvimento(Integer projectKey, EstagioDesenvolvimentoEnum estagio) {
        List<UserStory> userStoriesByEstagioDesenvolvimento = new ArrayList<>();

        for (UserStory us : this.getUsersStoriesByProject(projectKey)) {
            if (us.getEstagioDesenvolvimento().equals(estagio)) userStoriesByEstagioDesenvolvimento.add(us);
        }
        return userStoriesByEstagioDesenvolvimento;
    }


    private Collection<UserStory> listAllUserStoriesFromUserByEstagioDesenvolvimento(Integer projectKey, EstagioDesenvolvimentoEnum estagio, String username) {
        Collection<UserStory> listaDeUserStoriesPorStatus = listAllUserStoriesByEstagioDesenvolvimento(projectKey, estagio);
        Collection<UserStory> listaDeUserStoriesDoUsuarioPorStatus = new ArrayList<>();

        for (UserStory us : listaDeUserStoriesPorStatus) {
            if (us.getResponsaveis().containsUsername(username)) listaDeUserStoriesDoUsuarioPorStatus.add(us);
        }
        return listaDeUserStoriesDoUsuarioPorStatus;
    }

    private Collection<String> listaIntegrantesDeUmaUserStory(Integer projectKey, Integer userStoryId) {
        return this.projetoRepository.getProjeto(projectKey).getUserStoryRepository().getUserStory(userStoryId).getResponsaveis().getIntegrantes();
    }

    private Collection<UserStory> getUsersStoriesByProject(Integer projectKey) {
        return projetoRepository.getProjeto(projectKey).getUserStoryRepository().getAll();
    }

    private int getTotalDeUserStoriesByProject(Integer projectKey) {
        return projetoRepository.getProjeto(projectKey).getUserStoryRepository().getAll().size();
    }

    private int getTotalDeUserStoriesByIntegrante(Integer projectKey, String username) {
        Collection<UserStory> listaDeUserStories = getUsersStoriesByProject(projectKey);
        List<UserStory> listaDeUserStoriesDoUsuario = new ArrayList<>();

        for (UserStory userStory : listaDeUserStories) {
            if (listaIntegrantesDeUmaUserStory(projectKey, userStory.getId()).contains(username)) {
                listaDeUserStoriesDoUsuario.add(userStory);
            }
        }
        return listaDeUserStoriesDoUsuario.size();
    }


    public String listaRelatorioDeUsersStories(Integer projectKey, String username) throws ProjetoNotFoundException, UsuarioNotFoundException, UsuarioNotAllowedException {
        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!(this.projetoService.contemIntegrante(projectKey, username))) {
            throw new UsuarioNotFoundException("Usuário não é integrante deste projeto");
        } else if (!this.projetoService.getIntegranteByUserName(projectKey, username).getPapel().getTipo().equals(PapelEnum.PRODUCT_OWNER)) {
            throw new UsuarioNotAllowedException("Apenas Product Owners podem requisitar este relatório");
        }

        int userStoriesTotal = getTotalDeUserStoriesByProject(projectKey);

        int totalUserStoriesToDo = listAllUserStoriesByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.TO_DO).size();
        int totalUserStoriesWorkInProgress = listAllUserStoriesByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.WORK_IN_PROGRESS).size();
        int totalUserStoriesToVerify = listAllUserStoriesByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.TO_VERIFY).size();
        int totalUserStoriesDone = listAllUserStoriesByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.DONE).size();

        String percentualUserStoriesToDo = String.format("%.2f", ((float) totalUserStoriesToDo / userStoriesTotal) * 100);
        String percentualUserStoriesWorkInProgress = String.format("%.2f", ((float) totalUserStoriesWorkInProgress / userStoriesTotal) * 100);
        String percentualUserStoriesToVerify = String.format("%.2f", ((float) totalUserStoriesToVerify / userStoriesTotal) * 100);
        String percentualUserStoriesDone = String.format("%.2f", ((float) totalUserStoriesDone / userStoriesTotal) * 100);

        if (userStoriesTotal == 0) return "Não há User Stories atribuídas a esse projeto.";

        return "Percentual e total de User Stories em cada estágio de desenvolvimento: \n\n" + "To Do: " + percentualUserStoriesToDo + "% esse percentual representa um total de: " + totalUserStoriesToDo + " User Storys\n" + "Work In Progress: " + percentualUserStoriesWorkInProgress + "% esse percentual representa um total de: " + totalUserStoriesWorkInProgress + " User Storys\n" + "To Verify: " + percentualUserStoriesToVerify + "% esse percentual representa um total de: " + totalUserStoriesToVerify + " User Storys\n" + "Done: " + percentualUserStoriesDone + "% esse percentual representa um total de: " + totalUserStoriesDone + " User Storys\n";
    }

    public String listaRelatorioDeUsersStoriesDeUmProjeto(Integer projectKey, String username) throws ProjetoNotFoundException, UsuarioNotAllowedException, UsuarioNotFoundException, UserStoryNotFoundException {

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!(this.projetoService.getScrumMasterName(projectKey).equals(username))) {
            throw new UsuarioNotAllowedException("Usuário não é o Scrum Master deste projeto");
        } else if (this.getTotalDeUserStoriesByProject(projectKey) == 0) {
            throw new UserStoryNotFoundException("Não há User Stories atribuídas a esse projeto.");
        }

        String relatorio = "";

        for (String integrante : this.projetoRepository.getProjeto(projectKey).getIntegranteRepository().getIntegrantes()) {
            if (!(this.projetoService.getIntegranteByUserName(projectKey, integrante).getPapel().getTipo().equals(PapelEnum.PRODUCT_OWNER) || this.projetoService.getIntegranteByUserName(projectKey, integrante).getPapel().getTipo().equals(PapelEnum.SCRUM_MASTER))) {
                relatorio += listaRelatorioDeUsersStoriesDeUmUsuario(projectKey, integrante) + "\n";
            }
        }
        return relatorio;
    }
}
