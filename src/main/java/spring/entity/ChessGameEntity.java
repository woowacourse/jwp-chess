package spring.entity;

import chess.board.ChessBoard;
import chess.game.ChessGame;
import chess.team.Team;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import spring.entity.util.ChessBoardConverter;

import java.util.Set;

@Table("chessgame2")
public class ChessGameEntity {
    @Id
    Long id;
    @Column("turn_is_black")
    Boolean isTurnBlack;
    Set<PieceEntity> pieces;

    public ChessGameEntity(Long gameId, Boolean isTurnBlack, Set<PieceEntity> pieces) {
        this.id = gameId;
        this.isTurnBlack = isTurnBlack;
        this.pieces = pieces;
    }

    public ChessGameEntity(Boolean isTurnBlack, Set<PieceEntity> pieces) {
        this.isTurnBlack = isTurnBlack;
        this.pieces = pieces;
    }

    public ChessGame toChessGame() {
        Team turn = Team.of(isTurnBlack);
        ChessBoard chessBoard = ChessBoardConverter.convert(pieces);
        return new ChessGame(chessBoard, turn);
    }

    public void setId(Long id) {
        this.id = id;
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

    public void updateTurn() {
        this.isTurnBlack = !isTurnBlack;
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

    private ChessGameEntity() {
    }
}
