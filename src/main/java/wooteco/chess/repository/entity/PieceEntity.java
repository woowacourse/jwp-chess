package wooteco.chess.repository.entity;

import java.io.Serializable;

import org.springframework.data.relational.core.mapping.Table;

@Table("piece")
public class PieceEntity implements Serializable {
    private final String name;
    private final String color;
    private final String position;

    public PieceEntity(final String name, final String color, final String position) {
        this.name = name;
        this.color = color;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getPosition() {
        return position;
    }
}
