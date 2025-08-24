package br.com.taskboard.model;

import java.time.LocalDateTime;
public class Card {

    private int id;
    private int columnId;
    private String titulo;
    private String descricao;
    private LocalDateTime dataCriacao;
    private boolean bloqueado;
    private String motivoBloqueio;
    private LocalDateTime dataEntradaColuna;
    private LocalDateTime dataSaidaColuna;


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }


    public int getColumnId() { return columnId; }
    public void setColumnId(int columnId) { this.columnId = columnId; }


    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }


    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }


    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }


    public boolean isBloqueado() { return bloqueado; }
    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }


    public String getMotivoBloqueio() { return motivoBloqueio; }
    public void setMotivoBloqueio(String motivoBloqueio) { this.motivoBloqueio = motivoBloqueio; }


    public LocalDateTime getDataEntradaColuna() { return dataEntradaColuna; }
    public void setDataEntradaColuna(LocalDateTime dataEntradaColuna) { this.dataEntradaColuna = dataEntradaColuna; }


    public LocalDateTime getDataSaidaColuna() { return dataSaidaColuna; }
    public void setDataSaidaColuna(LocalDateTime dataSaidaColuna) { this.dataSaidaColuna = dataSaidaColuna; }
}
