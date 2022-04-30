package chess.web.dao;

import chess.board.piece.Piece;
import java.util.List;
import java.util.Optional;

public interface PieceDao {

    void save(Piece piece, Long roomId);

    void updatePieceByPositionAndRoomId(final String type, final String team, final String position,
                                        final Long roomId);

    Optional<Piece> findByPositionAndRoomId(final String position, final Long roomId);

    List<Piece> findAllByRoomId(final Long roomId);

    void save(final List<Piece> pieces, final Long roomId);

    void deleteAllByRoomId(Long roomId);
}
