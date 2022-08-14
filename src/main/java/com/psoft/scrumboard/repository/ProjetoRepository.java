package com.psoft.scrumboard.repository;

import com.psoft.scrumboard.model.Projeto;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Repository
public class ProjetoRepository {

    private Map<Integer, Projeto> projetos;

    public ProjetoRepository() {this.projetos = new HashMap<Integer, Projeto>();}

    //numero 20000000 deve ser ajustado de acordo com a regra de negocios
    public int addProjeto(Projeto projeto) {
        Random random = new Random();

        Integer key = random.nextInt(20000000);
        this.projetos.put(key, projeto);

        return key;

    }

    public boolean containsProjectKey(Integer projectKey) {
        return this.projetos.containsKey(projectKey);
    }

    public Projeto getProjeto(Integer projectKey) {
        return this.projetos.get(projectKey);
    }

    public Collection<Projeto> getProjetos() {
        return this.projetos.values();
    }

    public void delProject(Integer projectKey) {
        this.projetos.remove(projectKey);
    }

    public boolean containsProjectname(String projectName) {
        for (Projeto projeto: projetos.values()){
            if(projeto.getNome().equals(projectName)){
                return true;
            }
        }
        return false;
    }
}
