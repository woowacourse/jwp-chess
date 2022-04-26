package chess.repository;

import chess.model.piece.Team;
import chess.model.status.Status;

public interface BoardRepository<T> {

    T save(T board);

    int deleteById(int id);

    T getById(int id);

    int updateStatus(int boardId, Status status);

    int updateTeamById(int boardId, Team team);

    Status getStatusById(int boardId);

    Team getTeamById(int roomId);
}
