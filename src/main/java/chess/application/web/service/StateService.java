package chess.application.web.service;

import chess.application.web.dao.CommandDao;
import chess.state.Start;
import chess.state.State;
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
        for (String command : commandDao.findAll()) {
            state = state.proceed(command);
        }
        return state;
    }

    public void initCommandRecord() {
        commandDao.clear();
    }

    public List<String> getAllCommand() {
        return commandDao.findAll();
    }

    public void insertCommand(String command) {
        commandDao.insert(command);
    }
}
