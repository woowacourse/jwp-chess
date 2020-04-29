package spring.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import spring.chess.board.ChessBoard;
import spring.chess.game.ChessGame;
import spring.chess.team.Team;
import spring.entity.converter.ChessBoardConverter;

import java.util.Set;

@Table("chessgame2")
public class ChessGameEntity {
    @Id
    Long id;
    @Column("turn_is_black")
    Boolean isTurnBlack;
    Set<PieceEntity> pieces;

    public ChessGameEntity(Boolean isTurnBlack, Set<PieceEntity> pieces) {
        this.isTurnBlack = isTurnBlack;
        this.pieces = pieces;
    }

    public ChessGame toChessGame() {
        Team turn = Team.of(isTurnBlack);
        ChessBoard chessBoard = ChessBoardConverter.convert(pieces);
        return new ChessGame(chessBoard, turn);
    }

    public Long getId() {
        return id;
    }

    public Boolean getTurnBlack() {
        return isTurnBlack;
    }

    public Set<PieceEntity> getPieces() {
        return pieces;
    }

    public void add(PieceEntity pieceEntity) {
        pieces.add(pieceEntity);
    }

    public void delete(PieceEntity pieceEntity) {
        pieces.remove(pieceEntity);
    }

    public void update(Set<PieceEntity> pieces) {
        this.pieces = pieces;
    }
}
