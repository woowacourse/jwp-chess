package chess.dao;

import chess.domain.Team;
import chess.dto.request.GameIdRequest;
import chess.dto.request.MakeRoomRequest;
import chess.dto.response.RoomResponse;
import chess.dto.response.RoomStatusResponse;

import java.util.List;

public interface RoomDao {

    void makeGame(Team team, MakeRoomRequest makeRoomRequest);

    List<RoomResponse> getGames();

    RoomStatusResponse findById(MakeRoomRequest makeRoomRequest);

    RoomResponse findById(GameIdRequest gameIdRequest);

    void updateStatus(Team team, long roomId);

    void deleteGame(long roomId);
}
