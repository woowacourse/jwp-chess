package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.ReadRoomResultDto;

public interface RoomDao {

    void saveTurn(Color color, int roomId);

    Player getPlayer(int roomId);

    void deleteAll(int roomId);

    int createRoom(CreateRoomRequestDto createRoomRequestDto);

    ReadRoomResultDto findAll();
}
