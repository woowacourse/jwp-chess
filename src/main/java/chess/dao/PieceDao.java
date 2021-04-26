package chess.dao;

import chess.dao.dto.piece.PieceDto;

import java.util.List;

public interface PieceDao {
    Long save(PieceDto pieceDto);

    long[] savePieces(final Long gameId, final List<PieceDto> pieceDtos);

    List<PieceDto> findPiecesByGameId(final Long gameId);

    Long updateByGameIdAndPosition(PieceDto pieceDto);
}
