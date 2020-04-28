package wooteco.chess.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("piece")
public class PieceEntity {
    private String name;
    private String color;
    private String position;

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
