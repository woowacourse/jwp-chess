package chess.domain.repository;

import chess.domain.dao.CommandDao;
import chess.domain.dao.HistoryDao;
import chess.domain.dto.CommandDto;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameRepositoryImpl implements ChessGameRepository {

    private CommandDao commandDaoImpl;
    private HistoryDao historyDaoImpl;

    public ChessGameRepositoryImpl(CommandDao commandDaoImpl, HistoryDao historyDaoImpl) {
        this.commandDaoImpl = commandDaoImpl;
        this.historyDaoImpl = historyDaoImpl;
    }

    @Override
    public void insertCommand(CommandDto commandDto, int id) {
        commandDaoImpl.insert(commandDto, id);
    }

    @Override
    public List<CommandDto> selectAllCommands(String id) {
        return commandDaoImpl.selectAllCommands(id);
    }

    @Override
    public void insertHistory(String name) {
        historyDaoImpl.insert(name);
    }

    @Override
    public Optional<Integer> findHistoryIdByName(String name) {
        return historyDaoImpl.findIdByName(name);
    }

    @Override
    public int deleteHistory(String name) {
        return historyDaoImpl.delete(name);
    }

    @Override
    public List<String> selectActiveHistory() {
        return historyDaoImpl.selectActive();
    }

    @Override
    public void updateEndStateHistory(String id) {
        historyDaoImpl.updateEndState(id);
    }
}
