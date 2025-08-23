package br.com.taskboard.model;

import java.util.List;
public class Column {

    private int id;
    private String nome;
    private int ordem;
    private ColumnType tipo; // INICIAL, CANCELAMENTO, FINAL, PENDENTE
    private List<Card> cards;

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

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public ColumnType getTipo() {
        return tipo;
    }

    public void setTipo(ColumnType tipo) {
        this.tipo = tipo;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
