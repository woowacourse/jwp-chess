package chess.web.dao;

import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import chess.web.dto.RoomDto;
import java.util.List;

public interface RoomDao {

    int save(RoomName name, RoomPassword password);

    List<RoomDto> findAll();

    void deleteById(int id);

    boolean confirmPassword(int id, String password);
}
