package wooteco.chess.domain.board;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import wooteco.chess.domain.piece.EmptyPiece;
import wooteco.chess.domain.piece.GamePiece;
import wooteco.chess.domain.piece.GamePieces;

public class BoardFactory {

    public static Board EMPTY_BOARD = Board.of(createEmptyMap());

    private static Map<Position, GamePiece> createEmptyMap() {
        return Position.list()
            .stream()
            .collect(Collectors.toMap(Function.identity(), position -> EmptyPiece.getInstance()));
    }

    public static Board createInitialBoard() {
        Map<Position, GamePiece> initialBoard = createEmptyMap();
        for (GamePiece piece : GamePieces.createGamePieces()) {
            placePiecesOnInitialPositions(initialBoard, piece);
        }

        return Board.of(initialBoard);
    }

    private static void placePiecesOnInitialPositions(Map<Position, GamePiece> board,
        GamePiece piece) {
        for (Position position : piece.getOriginalPositions()) {
            board.put(position, piece);
        }
    }

    public static Board of(Map<Position, GamePiece> boardInput) {
        Map<Position, GamePiece> board = new HashMap<>();

        for (Map.Entry<Position, GamePiece> entry : boardInput.entrySet()) {
            board.put(entry.getKey(), entry.getValue());
        }

        return Board.of(board);
    }
}
