package chess.repository;

import chess.dto.request.ChessRequestDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.request.TurnRequestDto;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public interface ChessRepository {
    void initializePieceStatus(final Map<String, String> board) throws SQLException;

    void initializeTurn() throws SQLException;

    List<ChessRequestDto> showAllPieces() throws SQLException;

    List<TurnRequestDto> showCurrentTurn() throws SQLException;

    void movePiece(final MoveRequestDto moveRequestDto) throws SQLException;

    void changeTurn(final TurnChangeRequestDto turnChangeRequestDto) throws SQLException;

    void removeAllPieces() throws SQLException;

    void removeTurn() throws SQLException;

    void removePiece(final MoveRequestDto moveRequestDto) throws SQLException;
}
