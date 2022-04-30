package chess.dto;

import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardDto {

    private Map<String, PieceDto> board;

    public BoardDto(Map<String, PieceDto> board) {
        this.board = board;
    }

    public ChessBoard toEntity() {
        Map<Position, Piece> board = this.board.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Position.of(entry.getKey()),
                        entry -> entry.getValue().toEntity()));
        return new ChessBoard(board);
    }

    public Map<String, PieceDto> getBoard() {
        return board;
    }
}
