package wooteco.chess.repository.entity;

import java.io.Serializable;
import java.util.Set;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.Color;

@Table("game")
public class GameEntity implements Serializable {
    private Color turn;
    @Column("game")
    private Set<PieceEntity> piece;

    public GameEntity(final Color turn, final Set<PieceEntity> piece) {
        this.turn = turn;
        this.piece = piece;
    }

    public Color getTurn() {
        return turn;
    }

    public Set<PieceEntity> getPiece() {
        return piece;
    }

    public void updatePiece(final Set<PieceEntity> piece) {
        this.piece = piece;
    }

    public void updateTurn(final Color turn) {
        this.turn = turn;
    }
}
