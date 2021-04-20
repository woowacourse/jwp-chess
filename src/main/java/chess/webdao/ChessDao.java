package chess.webdao;

import chess.domain.ChessGame;

public interface ChessDao {
    ChessGame createChessGame();
    ChessGame readChessGame();
    void updateChessGame(final ChessGame chessGame, final String currentTurnTeam);
    void deleteChessGame();
}