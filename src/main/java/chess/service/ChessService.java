package chess.service;

import chess.controller.web.dto.MoveRequestDto;
import chess.domain.manager.ChessGameManager;
import chess.domain.manager.ChessGameManagerBundle;
import chess.domain.piece.attribute.Color;
import chess.domain.position.Position;
import chess.domain.statistics.ChessGameStatistics;

public interface ChessService {
    ChessGameManager start(String title);

    ChessGameManager end(long gameId);

    ChessGameManagerBundle findRunningGames();

    ChessGameManager load(long gameId);

    void move(long gameId, Position from, Position to);

    void move(long gameId, MoveRequestDto moveRequestDto);

    boolean isEnd(long gameId);

    ChessGameManager findById(long gameId);

    Color nextColor(long gameId);

    ChessGameStatistics getStatistics(long gameId);

    ChessGameManager reset(long gameId);

    void delete(long id);
}
