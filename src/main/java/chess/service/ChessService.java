package chess.service;

import chess.domain.piece.Piece;
import chess.domain.player.Round;
import chess.domain.position.Position;
import chess.dto.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;
import java.util.Queue;

@Service
public interface ChessService {
    Round makeRound();

    Map<String, String> chessBoardFromDB() throws SQLException;

    Map<Position, Piece> chessBoard(final Map<String, String> chessBoardFromDB) throws SQLException;

    Map<Position, Piece> chessBoard();

    Map<String, String> stringChessBoard(final Map<Position, Piece> chessBoard) throws SQLException;

    PiecesDto piecesDto(final Map<Position, Piece> chessBoard);

    void updateRound(final PiecesDto piecesDto);

    String currentTurn() throws SQLException;

    void changeRoundState(final String currentTurn);

    PlayerDto playerDto();

    ScoreDto scoreDto(final PlayerDto playerDto);

    void changeRoundToEnd(final PlayerDto playerDto);

    void executeRound(final Queue<String> commands);

    void movePiece(final MoveRequestDto moveRequestDto) throws SQLException;

    void changeTurn(final TurnChangeRequestDto turnChangeRequestDto) throws SQLException;

    void remove() throws SQLException;

    Map<String, String> filteredChessBoard(final Map<Position, Piece> chessBoard);

    void initialize(final Map<String, String> filteredChessBoard) throws SQLException;
}
