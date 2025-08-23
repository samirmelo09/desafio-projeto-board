package br.com.taskboard.model;

import java.util.List;

public class Board {

    private int id;
    private String nome;
    private List<Column> colunas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Column> getColunas() {
        return colunas;
    }

    public void setColunas(List<Column> colunas) {
        this.colunas = colunas;
    }
}
