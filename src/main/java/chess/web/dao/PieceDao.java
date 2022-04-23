package chess.web.dao;

import chess.board.piece.Piece;

import java.util.List;
import java.util.Optional;

public interface PieceDao {

    void save(Piece piece, Long boardId);

    void updatePieceByPositionAndBoardId(final String type, final String team, final String position, final Long boardId);

    Optional<Piece> findByPositionAndBoardId(final String position, final Long boardId);

    List<Piece> findAllByBoardId(final Long boardId);

    void save(final List<Piece> pieces, final Long boardId);

    void deleteByBoardId(Long boardId);
}
