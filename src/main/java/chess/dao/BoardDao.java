package chess.dao;

import chess.domain.board.Board;
import chess.dto.request.CreatePieceDto;
import chess.dto.request.DeletePieceDto;
import chess.dto.request.UpdatePiecePositionDto;

public interface BoardDao {
    Board getBoard(int gameId);

    void createPiece(CreatePieceDto createPieceDto);

    void deletePiece(DeletePieceDto deletePieceDto);

    void deletePieces(int gameId);

    void updatePiecePosition(UpdatePiecePositionDto updatePiecePositionDto);
}
