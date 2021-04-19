package chess.dao;

import chess.dao.dto.piece.PieceDto;
import chess.domain.board.position.Position;
import chess.domain.piece.Piece;

import java.util.List;
import java.util.Map;

public interface PieceDao {
    long[] savePieces(final Long gameId, final Map<Position, Piece> pieces);

    Long updateSourcePiece(final String source, final Long gameId);

    Long updateTargetPiece(final String target, Piece sourcePiece, final Long gameId);

    List<PieceDto> findPiecesByGameId(final Long gameId);
}
