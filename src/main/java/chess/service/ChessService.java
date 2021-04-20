package chess.service;

import chess.controller.web.dto.MoveRequestDto;
import chess.chessgame.domain.manager.ChessGameManager;
import chess.chessgame.domain.manager.ChessGameManagerBundle;
import chess.chessgame.domain.piece.attribute.Color;
import chess.chessgame.domain.position.Position;
import chess.chessgame.domain.statistics.ChessGameStatistics;

public interface ChessService {
    ChessGameManager start();

    ChessGameManager end(long gameId);

    ChessGameManagerBundle findRunningGames();

    boolean isKindDead(long gameId);

    ChessGameManager load(long gameId);

    void move(long gameId, Position from, Position to);

    void move(MoveRequestDto moveRequestDto);

    boolean isEnd(long gameId);

    ChessGameManager findById(long gameId);

    Color nextColor(long gameId);

    ChessGameStatistics getStatistics(long gameId);
}
