package chess.dao;

import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.dto.MoveDto;
import chess.dto.PieceDto;
import java.util.List;

public interface PieceDao {

    void insertAllPieces(final int id, final Player player);

    List<PieceDto> findPiecesByTeam(final int id, final Team team);

    void updatePiece(final int id, final MoveDto moveDto);

    void removePieceByCaptured(final int id, final MoveDto moveDto);

    void deletePieces(final int id);

    int getKingCount(int roomId);
}
