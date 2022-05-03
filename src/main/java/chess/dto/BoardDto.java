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

    public static BoardDto from(ChessBoard chessBoard) {
        Map<Position, Piece> pieces = chessBoard.getPieces();
        Map<String, PieceDto> board = pieces.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getValue(),
                        entry -> PieceDto.from(entry.getValue())));
        return new BoardDto(board);
    }

    public Map<String, PieceDto> getBoard() {
        return board;
    }
}
