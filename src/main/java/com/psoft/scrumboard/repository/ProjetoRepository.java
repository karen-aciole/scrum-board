package com.psoft.scrumboard.repository;

import com.psoft.scrumboard.model.Projeto;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ProjetoRepository {

    private Map<String, Projeto> projetos;

    public ProjetoRepository() {
        this.projetos = new HashMap<String, Projeto>();
    }

    public void addProjeto(Projeto projeto) {
        this.projetos.put(projeto.getNome(), projeto);
    }

    public boolean containsProjectname(String projectname) {
        return this.projetos.containsKey(projectname);
    }

    public Projeto getProjeto(String projectname) {
        return this.projetos.get(projectname);
    }

    public void delProject(String projectname) {
        this.projetos.remove(projectname);
    }

}
