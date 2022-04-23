package chess.dao.game;

import chess.domain.Board;
import chess.domain.piece.Piece;

public interface PieceDao {

    void save(Long gameId, Piece piece);

    void deletePiecesByGameId(Long gameId);

    Board findBoardByGameId(Long gameId);

    void move(final Long gameId, final String rawFrom, final String rawTo);
}
