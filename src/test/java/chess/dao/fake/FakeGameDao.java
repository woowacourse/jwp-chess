package chess.dao.fake;

import chess.dao.GameDao;
import chess.dto.GameDto;
import java.util.List;

public class FakeGameDao implements GameDao {
    @Override
    public int create(String roomTitle, String password) {
        return 0;
    }

    @Override
    public List<GameDto> find() {
        return null;
    }

    @Override
    public void delete(int id) {

    }
}
