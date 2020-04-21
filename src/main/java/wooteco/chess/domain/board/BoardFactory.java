package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BoardFactory {
    public static Board create() {
        Map<Position, Piece> board = new HashMap<>();

        for (Position position : Position.positions) {
            putIfPresent(board, position);
        }

        return new Board(board);
    }

    private static void putIfPresent(Map<Position, Piece> board, Position position) {
        Optional<Piece> candidate = findInitialPiece(position);

        if (candidate.isPresent()) {
            board.put(position, candidate.get());
        }
    }

    private static Optional<Piece> findInitialPiece(Position position) {
        return PieceRepository.pieces()
                .stream()
                .filter(piece -> piece.isInitialPosition(position))
                .findAny();
    }
}
