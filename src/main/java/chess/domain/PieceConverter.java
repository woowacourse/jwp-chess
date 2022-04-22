package chess.domain;

import chess.domain.piece.Piece;
import chess.domain.piece.multiple.Bishop;
import chess.domain.piece.multiple.Queen;
import chess.domain.piece.multiple.Rook;
import chess.domain.piece.pawn.Pawn;
import chess.domain.piece.single.King;
import chess.domain.piece.single.Knight;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;

public enum PieceConverter {

    KING("king", (chessGameId, color) -> new Piece(chessGameId, color, new King())),
    QUEEN("queen", (chessGameId, color) -> new Piece(chessGameId, color, new Queen())),
    BISHOP("bishop", (chessGameId, color) -> new Piece(chessGameId, color, new Bishop())),
    ROOK("rook", (chessGameId, color) -> new Piece(chessGameId, color, new Rook())),
    KNIGHT("knight", (chessGameId, color) -> new Piece(chessGameId, color, new Knight())),
    PAWN("pawn", (chessGameId, color) -> new Piece(chessGameId, color, new Pawn(color))),
    ;

    private final String pieceName;
    private final BiFunction<Long, Color, Piece> pieceCreator;

    PieceConverter(String pieceName, BiFunction<Long, Color, Piece> pieceCreator) {
        this.pieceName = pieceName;
        this.pieceCreator = pieceCreator;
    }

    public static Piece parseToPiece(String name, Long chessGameId, Color color) {
        Objects.requireNonNull(name, "name은 null이 들어올 수 없습니다.");
        return Arrays.stream(values())
                .filter(pieceConvertor -> pieceConvertor.pieceName.equals(name))
                .findAny()
                .map(pieceConvertor -> pieceConvertor.pieceCreator.apply(chessGameId, color))
                .orElseThrow(() -> new IllegalArgumentException("없는 기물 정보입니다."));
    }
}
