package chess.dao;

import chess.domain.ChessGame;
import chess.domain.CurrentGameRoom;
import chess.domain.TeamColor;
import chess.domain.piece.Piece;
import java.util.List;
import java.util.Optional;

public interface ChessDAO {
    void deleteAllByGameId(Long gameId);

    Optional<TeamColor> currentTurnByGameId(Long gameId);

    List<ChessDto> chessByGameId(Long gameId);

    void savePieces(Long gameId, List<Piece> pieces);

    Long saveCurrentColor(Long gameId, TeamColor teamColor);
}