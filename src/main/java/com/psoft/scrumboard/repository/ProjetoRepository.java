package com.psoft.scrumboard.repository;

import com.psoft.scrumboard.model.Projeto;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Repository
public class ProjetoRepository {

    private Map<Integer, Projeto> projetos;

    public ProjetoRepository() {this.projetos = new HashMap<Integer, Projeto>();}

    //numero 200 deve ser ajustado de acordo com a regra de negocios
    public int addProjeto(Projeto projeto) {
        Random random = new Random();

        Integer key = random.nextInt(200);
        this.projetos.put(key, projeto);

        return key;

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
