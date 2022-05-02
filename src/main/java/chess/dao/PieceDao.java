package chess.dao;

import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.dto.MoveDto;
import chess.dto.PieceDto;

import java.util.List;

public interface PieceDao {

    void initializePieces(final long roomId, final Player player);

    List<PieceDto> findPiecesByTeam(final long roomId, final Team team);

    void updatePiece(final long roomId, final MoveDto moveDto);

    void removePieceByCaptured(final long roomId, final MoveDto moveDto);

    void endPieces(final long roomId);
}
