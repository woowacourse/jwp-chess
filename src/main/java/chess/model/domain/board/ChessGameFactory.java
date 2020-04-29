package chess.model.domain.board;

import chess.model.domain.piece.Piece;
import chess.model.domain.piece.PieceFactory;
import chess.model.domain.piece.Team;
import chess.model.repository.BoardEntity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChessGameFactory {

    public static ChessGame create() {
        return new ChessGame(ChessBoard.createInitial(), Team.WHITE,
            CastlingElement.createInitial(), EnPassant.createEmpty());
    }

    public static ChessGame of(Team turn, Iterable<BoardEntity> boardEntities) {
        Map<Square, Piece> chessBoard = new HashMap<>();
        Set<CastlingSetting> castlingElements = new HashSet<>();
        Map<Square, Square> enPassants = new HashMap<>();

        for (BoardEntity boardEntity : boardEntities) {
            combineBoard(chessBoard, boardEntity);
            combineCastlingElements(castlingElements, boardEntity);
            combineEnPassants(enPassants, boardEntity);
        }

        return new ChessGame(ChessBoard.of(chessBoard), turn, CastlingElement.of(castlingElements),
            EnPassant.of(enPassants));
    }

    private static void combineEnPassants(Map<Square, Square> enPassants, BoardEntity boardEntity) {
        if (boardEntity.getEnPassantName() != null) {
            enPassants.put(Square.of(boardEntity.getEnPassantName()),
                Square.of(boardEntity.getSquareName()));
        }
    }

    private static void combineCastlingElements(Set<CastlingSetting> castlingElements,
        BoardEntity boardEntity) {
        if (boardEntity.getCastlingElementYN().equals("Y")) {
            castlingElements.add(CastlingSetting.of(Square.of(boardEntity.getSquareName()),
                PieceFactory.getPiece(boardEntity.getPieceName())));
        }
    }

    private static void combineBoard(Map<Square, Piece> chessBoard, BoardEntity boardEntity) {
        chessBoard.put(Square.of(boardEntity.getSquareName()),
            PieceFactory.getPiece(boardEntity.getPieceName()));
    }
}
