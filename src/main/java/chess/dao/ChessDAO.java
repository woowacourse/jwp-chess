package chess.dao;

import chess.domain.TeamColor;
import chess.domain.piece.Piece;
import java.util.List;
import java.util.Optional;

public interface ChessDAO {
    void deleteAllByGameId(Long gameId);

    Optional<TeamColor> findCurrentTurn(Long gameId);

    List<ChessDto> findChess(Long gameId);

    void savePieces(Long gameId, List<Piece> pieces);

    Long saveCurrentColor(Long gameId, TeamColor teamColor);
}