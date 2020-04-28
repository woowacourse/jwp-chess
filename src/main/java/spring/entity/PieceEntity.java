package spring.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("piece2")
public class PieceEntity {
    @Id
    Long id;
    String name;
    String row;
    String col;

    public PieceEntity(String name, String row, String col) {
        this.name = name;
        this.row = row;
        this.col = col;
    }

    public void update(String name, String row, String col) {
        this.name = name;
        this.row = row;
        this.col = col;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRow() {
        return row;
    }

    public String getCol() {
        return col;
    }
}
