package chess.service;

import chess.domain.ChessGame;
import chess.domain.Position;

public interface ChessService {

    ChessGame loadChess(Long gameId);

    void restart(Long gameId);

    void exitGame(Long gameId);

    void saveGame(Long gameId);

    void move(Long gameId, Position currentPosition, Position targetPosition);
}
