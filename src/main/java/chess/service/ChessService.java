package chess.service;

import chess.controller.dto.GameStatusDto;
import chess.domain.game.MoveRequest;
import chess.dao.CommandDao;
import chess.dao.GameDao;
import chess.domain.game.BoardFactory;
import chess.domain.game.Game;
import chess.exception.ChessException;
import chess.exception.ErrorCode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChessService {
    private final CommandDao commandDao;
    private final GameDao gameDao;

    public ChessService(CommandDao commandDao, GameDao gameDao) {
        this.commandDao = commandDao;
        this.gameDao = gameDao;
    }

    public void move(Long gameId, MoveRequest moveRequest) {
        Game game = loadGame(gameId);
        game.move(moveRequest);
        commandDao.insert(gameId, moveRequest.getFrom(), moveRequest.getTo());
    }

    public GameStatusDto load(Long gameId) {
        Game game = loadGame(gameId);
        return new GameStatusDto(game);
    }

    private Game loadGame(Long gameId) {
        checkGameExist(gameId);
        Game game = new Game(BoardFactory.create());
        List<MoveRequest> requests = commandDao.findAllCommandOf(gameId);
        for (MoveRequest request : requests) {
            game.move(request);
        }
        return game;
    }

    private void checkGameExist(Long gameId) {
        try {
            gameDao.findById(gameId);
        } catch (EmptyResultDataAccessException e) {
            throw new ChessException(ErrorCode.NO_ROOM_TO_LOAD);
        }
    }
}