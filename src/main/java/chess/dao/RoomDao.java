package chess.dao;

import chess.domain.Team;
import chess.entity.Room;
import java.util.List;
import java.util.Optional;

public interface RoomDao {

    Optional<Room> findById(Long roomId);

    void deleteBy(Long roomId, String password);

    Long save(String title, String password);

    void updateTeam(Team team, Long roomId);

    void updateStatus(Long roomId, boolean status);

    List<Room> findAll();
}
