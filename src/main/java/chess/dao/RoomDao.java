package chess.dao;

import chess.domain.Team;
import chess.dto.request.GameIdRequest;
import chess.dto.request.MakeRoomRequest;
import chess.entity.RoomEntity;

import java.util.List;

public interface RoomDao {

    void makeGame(Team team, MakeRoomRequest makeRoomRequest);

    List<RoomEntity> getGames();

    RoomEntity findById(MakeRoomRequest makeRoomRequest);

    RoomEntity findById(GameIdRequest gameIdRequest);

    boolean isExistId(GameIdRequest gameIdRequest);

    void updateStatus(Team team, long roomId);

    void deleteGame(long roomId);
}
