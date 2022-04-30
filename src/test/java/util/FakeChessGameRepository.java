package util;

import chess.domain.ChessGame;
import chess.domain.chessboard.ChessBoard;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.position.Position;
import chess.exception.NotFoundException;
import chess.repository.ChessGameRepository;
import java.util.HashMap;
import java.util.Map;

public class FakeChessGameRepository implements ChessGameRepository {

    private final Map<Integer, ChessGame> chessGameByRoomId = new HashMap<>();

    @Override
    public ChessGame get(final int roomId) {
        final ChessGame chessGame = chessGameByRoomId.get(roomId);
        if (chessGame == null) {
            throw new NotFoundException("기물이 존재하지 않습니다.");
        }
        return chessGame;
    }

    @Override
    public void update(final int roomId, final Position from, final Position to) {
        final ChessGame chessGame = chessGameByRoomId.get(roomId);
        final ChessBoard chessBoard = chessGame.getChessBoard();
        final Map<Position, ChessPiece> pieceByPosition = chessBoard.findAllPiece();
        final ChessPiece target = pieceByPosition.remove(from);
        pieceByPosition.put(to, target);
        chessGameByRoomId.put(roomId, chessGame);
    }

    @Override
    public void add(final int roomId, final ChessGame chessGame) {
        chessGameByRoomId.put(roomId, chessGame);
    }

    public void deleteAll() {
        chessGameByRoomId.clear();
    }
}
