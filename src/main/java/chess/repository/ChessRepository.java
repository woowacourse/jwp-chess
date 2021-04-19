package chess.repository;

import chess.dto.request.ChessRequestDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.request.TurnRequestDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ChessRepository {
    void initializePieceStatus(final Map<String, String> board);

    void initializeTurn();

    List<ChessRequestDto> showAllPieces();

    List<TurnRequestDto> showCurrentTurn();

    void movePiece(final MoveRequestDto moveRequestDto);

    void changeTurn(final TurnChangeRequestDto turnChangeRequestDto);

    void removeAllPieces();

    void removeTurn();

    void removePiece(final MoveRequestDto moveRequestDto);
}
