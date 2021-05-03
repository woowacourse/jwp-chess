package chess.domain.game;

import chess.dto.request.GameMoveRequest;

public interface ChessGameRepository {
    ChessGame chessGame(final Long gameId);
    Long create(final ChessGame chessGame);
    void save(final Long gameId, final ChessGame chessGame, GameMoveRequest moveDto);
}
