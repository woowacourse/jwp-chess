package chess.service;

import chess.dao.BoardDao;
import chess.domain.board.Board;
import chess.domain.game.GameId;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.Map;

public class BoardDaoFake implements BoardDao {
    private final Map<Position, Piece> fakeBoard = new HashMap<>();

    @Override
    public Board getBoard(GameId gameId) {
        return Board.from(new HashMap<>(fakeBoard));
    }

    @Override
    public void createPiece(GameId gameId, Position position, Piece piece) {
        fakeBoard.put(position, piece);
    }

    @Override
    public void deletePiece(GameId gameId, Position position) {
        fakeBoard.remove(position);
    }

    @Override
    public void updatePiecePosition(GameId gameId, Position from, Position to) {
        fakeBoard.put(to, fakeBoard.remove(from));
    }

    @Override
    public String toString() {
        return "BoardDaoFake{" +
                "fakeBoard=" + fakeBoard +
                '}';
    }
}
