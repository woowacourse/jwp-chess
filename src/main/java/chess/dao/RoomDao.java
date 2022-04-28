package chess.dao;

import chess.domain.Team;
import chess.dto.GameIdDto;
import chess.dto.MakeRoomDto;
import chess.dto.RoomDto;
import chess.dto.RoomStatusDto;

import java.util.List;

public interface RoomDao {

    void makeGame(Team team, MakeRoomDto makeRoomDto);

    List<RoomDto> getGames();

    RoomStatusDto findById(MakeRoomDto makeRoomDto);

    RoomDto findById(GameIdDto gameIdDto);

    void updateStatus(Team team, long roomId);

    void deleteGame(long roomId);
}
