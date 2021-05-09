package chess.domain.repository.board;

import chess.domain.board.Board;
import chess.domain.board.piece.Square;

import java.util.List;

public interface SquareRepository {
    Long save(Square square);

    long[] saveBoard(Long gameId, Board board);

    Square findById(Long id);

    List<Square> findByGameId(Long gameId);

    Long updateByGameIdAndPosition(Square square);
}
