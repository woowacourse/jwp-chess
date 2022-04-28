package chess.service;

import chess.repository.CommandDao;
import chess.domain.state.Start;
import chess.domain.state.State;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StateService {

    private final CommandDao commandDao;

    public StateService(CommandDao commandDao) {
        this.commandDao = commandDao;
    }

    public State currentState() {
        State state = Start.of();
        for (String command : commandDao.findAllInCommandTable()) {
            state = state.proceed(command);
        }
        return state;
    }

    public void initCommandRecord() {
        commandDao.clear();
    }

    public List<String> getAllCommand() {
        return commandDao.findAllInCommandTable();
    }

    public void insertCommand(String command) {
        commandDao.insert(command);
    }
}
