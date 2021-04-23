package chess.domain;

import chess.domain.game.ChessGame;

public interface ChessRepository {
    
    void save(ChessGame chessGame);
    void update(ChessGame chessGame);
    ChessGame findByGameId(String gameId);
    boolean containsByGameId(String chessGame);
    
}
