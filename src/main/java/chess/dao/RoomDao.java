package chess.dao;

import chess.domain.Team;
import chess.entity.Room;
import java.util.List;

public interface RoomDao {

    Room findById(Long roomId);

    void deleteBy(Long roomId, String password);

    Long save(String title, String password);

    void updateTeam(Team team, Long roomId);

    void updateStatus(Long roomId, boolean status);

    List<Room> findAll();
}
