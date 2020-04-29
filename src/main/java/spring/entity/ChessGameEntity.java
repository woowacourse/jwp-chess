package spring.entity;

import chess.board.ChessBoard;
import chess.game.ChessGame;
import chess.team.Team;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import spring.entity.converter.ChessBoardConverter;

import java.util.Set;

@Table("chessgame2")
public class ChessGameEntity {
    @Id
    Long id;
    String whiteName;
    String blackName;
    Boolean isTurnBlack;
    Set<PieceEntity> pieces;

    public ChessGameEntity(String whiteName, String blackName, Boolean isTurnBlack, Set<PieceEntity> pieces) {
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.isTurnBlack = isTurnBlack;
        this.pieces = pieces;
    }

    public ChessGame toChessGame() {
        Team turn = Team.of(isTurnBlack);
        ChessBoard chessBoard = ChessBoardConverter.convert(pieces);
        return new ChessGame(chessBoard, turn);
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
