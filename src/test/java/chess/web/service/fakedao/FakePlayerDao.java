package chess.web.service.fakedao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.dao.RoomDao;
import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.DeleteDto;
import chess.web.dto.DeleteResultDto;
import chess.web.dto.FinishResultDto;
import chess.web.dto.ReadRoomResultDto;
import chess.web.dto.RoomDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakePlayerDao implements RoomDao {

    List<Color> repository = new ArrayList<>() {{
        add(Color.WHITE);
    }};

    @Override
    public void saveTurn(Color color, int roomId) {
        repository.add(color);
    }

    @Override
    public Player getPlayer(int roomId) {
        return Player.of(repository.get(0));
    }

    @Override
    public int createRoom(CreateRoomRequestDto createRoomRequestDto) {

        return 0;
    }

    @Override
    public ReadRoomResultDto findAll() {
        return null;
    }

    @Override
    public void changeTurn(int roomId) {
    }

    @Override
    public FinishResultDto finish(int roomId) {
        return new FinishResultDto(roomId, true);
    }

    @Override
    public RoomDto isStartable(int roomId) {
        return null;
    }

    @Override
    public DeleteResultDto delete(int roomId, DeleteDto deleteDto) {
        return null;
    }
}
