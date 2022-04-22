package chess.dao;

import chess.domain.Team;
import chess.dto.RoomDto;

public interface RoomDao {
    RoomDto findById(long roomId);
    void delete(long roomId);
    void save(long roomId, Team team);
    void updateStatus(Team team, long roomId);
}
