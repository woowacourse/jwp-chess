package chess.web.dao;

import chess.web.dto.PlayerDto;
import java.util.Optional;

public interface PlayerRepository {
    void save(String name);

    Optional<PlayerDto> find(String name);
}
