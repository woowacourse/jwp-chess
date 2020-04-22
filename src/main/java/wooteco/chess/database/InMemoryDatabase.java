package wooteco.chess.database;

import wooteco.chess.dto.Commands;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDatabase {
    private List<Commands> commands;

    public InMemoryDatabase() {
        this.commands = new ArrayList<>();
    }

    public void add(Commands command) {
        commands.add(command);
    }

    public void clear() {
        commands.clear();
    }

    public List<Commands> get() {
        return commands;
    }
}
