package chess.domain;

import chess.domain.ChessGame;
import dto.MoveDto;

public interface ChessGameRepository {
    ChessGame chessGame(final Long gameId);
    Long create(final ChessGame chessGame);
    void save(final Long gameId, final ChessGame chessGame, MoveDto moveDto);
}
