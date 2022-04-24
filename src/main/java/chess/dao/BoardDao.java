package chess.dao;

import chess.domain.game.GameId;
import chess.domain.position.Position;
import chess.dto.request.CreatePieceDto;
import chess.dto.request.DeletePieceDto;
import chess.dto.request.UpdatePiecePositionDto;
import chess.dto.response.BoardDto;

public interface BoardDao {
    BoardDto getBoard(GameId gameId);

    void createPiece(CreatePieceDto createPieceDto);

    void deletePiece(DeletePieceDto deletePieceDto);

    void updatePiecePosition(GameId gameId, Position from, Position to);
}
