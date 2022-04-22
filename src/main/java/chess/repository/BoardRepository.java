package chess.repository;

import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.square.Square;

import java.util.Map;

public interface BoardRepository<T> {

    Board save(Board board);

    int deleteAll();

    int deleteById(int id);

    T getById(int id);

    T init(T board, Map<Square, Piece> startingPieces);

    int finishGame(int boardId);

    boolean isEnd(int boardId);

    int changeTurn(int boardId);
}
