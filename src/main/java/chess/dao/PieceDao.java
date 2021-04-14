package chess.dao;

import chess.controller.web.dto.piece.PieceResponseDto;
import chess.domain.board.position.Position;
import chess.domain.piece.Piece;

import java.util.List;
import java.util.Map;

public interface PieceDao {
    long[] savePieces(Long gameId, Map<Position, Piece> pieces);

    Long updateSourcePiece(String source, Long gameId);

    Long updateTargetPiece(String target, Piece sourcePiece, Long gameId);

    List<PieceResponseDto> findPiecesByGameId(Long gameId);
}
