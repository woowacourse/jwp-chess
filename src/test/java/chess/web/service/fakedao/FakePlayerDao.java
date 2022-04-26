package chess.web.service.fakedao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.dao.RoomDao;
import chess.web.dto.CreateRoomRequestDto;
import java.util.ArrayList;
import java.util.List;

public class FakePlayerDao implements RoomDao {

    List<Color> repository = new ArrayList<>() {{
        add(Color.WHITE);
    }};

    @Override
    public void save(Color color) {
        repository.add(color);
    }

    @Override
    public Player getPlayer() {
        return Player.of(repository.get(0));
    }

    @Override
    public void deleteAll() {
        repository.clear();
    }

    @Override
    public int createRoom(CreateRoomRequestDto createRoomRequestDto) {

        return 0;
    }
}
