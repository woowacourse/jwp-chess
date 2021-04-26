package chess.dao;

import chess.dao.dto.piece.PieceDto;
import chess.domain.board.position.Position;
import chess.domain.piece.Piece;

import java.util.List;
import java.util.Map;

public interface PieceDao {
    Long save(PieceDto pieceDto);

    long[] savePieces(final Long gameId, final List<PieceDto> pieceDtos);

    List<PieceDto> findPiecesByGameId(final Long gameId);

    Long updateByGameIdAndPosition(PieceDto pieceDto);
}
