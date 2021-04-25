package chess.domain.dao;

import chess.domain.dto.CommandDto;
import java.util.List;

public interface CommandDao {
    void insert(CommandDto commandDto, int id);
    List<CommandDto> selectAllCommands(String id);
}
