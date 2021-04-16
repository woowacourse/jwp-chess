package chess.web.dao.piece;

import chess.web.domain.piece.Piece;
import chess.web.domain.piece.type.PieceType;
import chess.web.domain.player.type.TeamColor;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;

@Repository
public interface PieceRepository {
    Piece findByPieceTypeAndTeamColor(PieceType pieceType, TeamColor teamColor) throws SQLException;
}
