package wooteco.chess.dao;

import wooteco.chess.database.InMemoryDatabase;
import wooteco.chess.dto.Commands;

import java.util.List;

public class InMemoryChessDao implements ChessDao {

    private final InMemoryDatabase commands = new InMemoryDatabase();

    @Override
    public void addCommand(Commands command) {
        commands.add(command);
    }

    @Override
    public void clearCommands() {
        commands.clear();
    }

    @Override
    public List<Commands> selectCommands() {
        return commands.get();
    }
}
