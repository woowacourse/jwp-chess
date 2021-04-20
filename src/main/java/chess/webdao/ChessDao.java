package chess.webdao;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.webdto.TurnDto;

import java.util.Map;

public interface ChessDao {
    int createChessGame(boolean isPlaying);

    int createTeamInfo(final String team, final Map<Position, Piece> teamPiecePosition);

    String readTeamInfo(final String team);

    TurnDto readTurn();

    void updateTeamInfo(final Map<Position, Piece> teamPiecePosition, final String team);

    void updateChessGame(final ChessGame chessGame, final String currentTurnTeam);

    void deleteChessGame();
}