package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.DeleteDto;
import chess.web.dto.DeleteResultDto;
import chess.web.dto.ReadRoomResultDto;
import chess.web.dto.RoomDto;
import chess.web.dto.FinishResultDto;

public interface RoomDao {

    void saveTurn(Color color, int roomId);

    Player getPlayer(int roomId);

    int createRoom(CreateRoomRequestDto createRoomRequestDto);

    ReadRoomResultDto findAll();

    void changeTurn(int roomId);

    FinishResultDto finish(int roomId);

    RoomDto isStartable(int roomId);

    DeleteResultDto delete(int roomId, DeleteDto deleteDto);
}
