package spring.entity;

import chess.location.Location;
import chess.piece.type.Piece;
import chess.piece.type.PieceMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("piece2")
public class PieceEntity {
    @Id
    Long id;
    String name;
    @Column("piece_row")
    String row;
    @Column("piece_col")
    String col;

    public PieceEntity(String name, String row, String col) {
        this.name = name;
        this.row = row;
        this.col = col;
    }

    public Location toLocation() {
        return new Location(Integer.parseInt(row), col.charAt(0));
    }

    public Piece toPiece() {
        return PieceMapper.of(name.charAt(0));
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
