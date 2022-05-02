package chess.dao;

import chess.domain.Team;
import chess.dto.request.GameIdRequest;
import chess.dto.request.RoomRequest;
import chess.entity.RoomEntity;

import java.util.List;

public interface RoomDao {

    void makeGame(Team team, RoomRequest roomRequest);

    List<RoomEntity> getGames();

    RoomEntity findById(RoomRequest roomRequest);

    RoomEntity findById(GameIdRequest gameIdRequest);

    boolean isExistId(GameIdRequest gameIdRequest);

    void updateStatus(Team team, long roomId);

    void deleteGame(long roomId);
}
