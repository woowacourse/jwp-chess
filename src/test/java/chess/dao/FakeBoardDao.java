package chess.dao;

import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.PieceDto;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeBoardDao implements BoardDao {

    private final Map<Integer, BoardDto> board;

    public FakeBoardDao() {
        this.board = new HashMap<>();
    }

    @Override
    public void save(ChessBoard chessBoard, int gameId) {
        Map<Position, Piece> pieces = chessBoard.getPieces();
        Map<String, PieceDto> board = new HashMap<>();
        for (Position position : pieces.keySet()) {
            String key = position.getValue();
            Piece piece = pieces.get(Position.of(key));
            PieceDto pieceDto = new PieceDto(piece.getSymbol().name(), piece.getColor().name());
            board.put(key, pieceDto);
        }
        this.board.put(gameId, new BoardDto(board));
    }

    @Override
    public ChessBoard findById(int id) {
        Map<String, PieceDto> board = this.board.get(id).getBoard();
        Map<Position, Piece> pieces = board.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Position.of(entry.getKey()),
                        entry -> entry.getValue().toEntity()));
        return new ChessBoard(pieces);
    }

    @Override
    public int update(Position position, Piece piece, int gameId) {
        BoardDto boardDto = board.get(gameId);
        String value = position.getValue();
        PieceDto pieceDto = new PieceDto(piece.getSymbol().name(), piece.getColor().name());
        Map<String, PieceDto> board = boardDto.getBoard();
        board.put(value, pieceDto);
        return gameId;
    }

    @Override
    public void delete(int gameId) {
        board.remove(gameId);
    }
}
