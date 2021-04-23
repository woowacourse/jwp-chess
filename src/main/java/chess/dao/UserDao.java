package chess.dao;

import chess.domain.board.Team;
import chess.dto.web.UsersInRoomDto;

public interface UserDao {

    void insert(String userName);

    UsersInRoomDto usersInRoom(String roomId);

    void updateStatistics(String roomId, Team winnerTeam);
}
