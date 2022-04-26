package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.ReadRoomResultDto;

public interface RoomDao {

    void save(Color color);

    Player getPlayer();

    void deleteAll();

    int createRoom(CreateRoomRequestDto createRoomRequestDto);

    ReadRoomResultDto findAll();
}
