package chess.web.service.fakedao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.dao.RoomDao;
import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.ReadRoomResultDto;
import java.util.ArrayList;
import java.util.List;

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
    public void deleteAll(int roomId) {
        repository.clear();
    }

    @Override
    public int createRoom(CreateRoomRequestDto createRoomRequestDto) {

        return 0;
    }

    @Override
    public ReadRoomResultDto findAll() {
        return null;
    }
}
