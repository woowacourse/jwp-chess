package chess.repository;

import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.piece.Team;
import chess.model.square.Square;
import chess.model.status.Status;

import java.util.Map;

public interface BoardRepository<T> {

    Board save(Board board);

    int deleteById(int id);

    T getById(int id);

    int updateStatus(int boardId, Status status);

    int updateTeamById(int boardId, Team team);

    Status getStatusById(int boardId);
}
