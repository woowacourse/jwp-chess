package chess.domain.repository;

import chess.domain.dto.CommandDto;
import java.util.List;
import java.util.Optional;

public interface ChessGameRepository {

    void insertCommand(CommandDto commandDto, int id);
    List<CommandDto> selectAllCommands(String id);
    void insertHistory(String name);
    Optional<Integer> findHistoryIdByName(String name);
    int deleteHistory(String name);
    List<String> selectActiveHistory();
    void updateEndStateHistory(String id);
}
