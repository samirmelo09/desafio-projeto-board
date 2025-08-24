package br.com.taskboard.model;

import java.util.List;
public class Column {

    private int id;
    private String name;
    private ColumnType type;
    private int boardId;

    // Construtor vazio (necessário para frameworks e JDBC)
    public Column() {
    }

    // Construtor completo
    public Column(int id, String name, ColumnType type, int boardId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.boardId = boardId;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    @Override
    public String toString() {
        return "Column{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", boardId=" + boardId +
                '}';
    }
}
