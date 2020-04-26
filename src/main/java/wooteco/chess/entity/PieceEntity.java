package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("board")
public class PieceEntity {
    @Id
    private Long id;

    @Column("position")
    private String position;

    @Column("pieceName")
    private String pieceName;

    public String getPosition() {
        return position;
    }

    public String getPieceName() {
        return pieceName;
    }
}
