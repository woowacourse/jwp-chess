package chess.service;

import chess.entity.CommandEntity;
import chess.repository.CommandDao;
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
}
