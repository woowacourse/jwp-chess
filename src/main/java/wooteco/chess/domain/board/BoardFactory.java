package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceRepository;
import wooteco.chess.domain.piece.Team;

import java.util.HashMap;
import java.util.Map;

public class BoardFactory {

    public static Board create() {
        Map<Position, Piece> board = new HashMap<>();

        for (Position position : Position.positions) {
            board.put(position, findInitialPiece(position));
        }
        return new Board(board, Team.WHITE);
    }

    private static Piece findInitialPiece(final Position position) {
        return PieceRepository.pieces()
                .stream()
                .filter(piece -> piece.isInitialPosition(position))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 위치에 적절한 말이 없습니다."));
    }
}
