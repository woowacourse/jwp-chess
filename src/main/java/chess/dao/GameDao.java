package chess.dao;

import chess.dto.GameDto;
import java.util.List;

public interface GameDao {
    int create(String roomTitle, String password);

    List<GameDto> find();

    void delete(int id);
}
