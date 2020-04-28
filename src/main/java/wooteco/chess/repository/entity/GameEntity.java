package wooteco.chess.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import wooteco.chess.domain.Color;

import java.util.Set;
import java.util.UUID;

@Table("game")
public class GameEntity {
    private Color turn;
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
