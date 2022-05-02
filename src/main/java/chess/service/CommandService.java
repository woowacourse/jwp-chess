package chess.service;

import chess.domain.state.Start;
import chess.domain.state.State;
import chess.entity.CommandEntity;
import chess.exception.DeleteRoomException;
import chess.repository.CommandDao;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CommandService {

    private final CommandDao commandDao;

    public CommandService(CommandDao commandDao) {
        this.commandDao = commandDao;
    }

    public CommandEntity create(Long roomId, String command) {
        return commandDao.insert(new CommandEntity(roomId, command));
    }

    public List<String> findAllByRoomID(Long roomId) {
        return commandDao.findAllByRoomId(roomId)
                .stream()
                .map(CommandEntity::getCommand)
                .collect(Collectors.toList());
    }

    public State getCurrentState(List<String> commands) {
        State state = Start.of();
        for (String command : commands) {
            state = state.proceed(command);
        }
        return state;
    }

    public void checkRunning(Long roomId) {
        if (getCurrentState(findAllByRoomID(roomId)).isRunning()) {
            throw new DeleteRoomException("아직 게임 중인 방입니다.");
        }
    }
}
