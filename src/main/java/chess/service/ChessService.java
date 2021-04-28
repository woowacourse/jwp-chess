package chess.service;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.game.ChessGameManagerBundle;
import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.game.board.position.Position;
import chess.chessgame.domain.room.game.statistics.ChessGameStatistics;

public interface ChessService {
    ChessGameManager start();

    ChessGameManager end(long gameId);

    ChessGameManagerBundle findRunningGames();

    boolean isKindDead(long gameId);

    ChessGameManager load(long gameId);

    void move(long gameId, Position from, Position to);

    boolean isEnd(long gameId);

    ChessGameManager findById(long gameId);

    Color nextColor(long gameId);

    ChessGameStatistics getStatistics(long gameId);
}
