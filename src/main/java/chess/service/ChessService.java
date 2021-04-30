package chess.service;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.game.board.position.Position;
import chess.chessgame.domain.room.game.statistics.ChessGameStatistics;

public interface ChessService {
    ChessGameManager load(long gameId);

    void move(long gameId, Color userColor, Position from, Position to);

    boolean isEnd(long gameId);

    ChessGameManager findById(long gameId);

    Color nextColor(long gameId);

    ChessGameStatistics getStatistics(long gameId);
}
