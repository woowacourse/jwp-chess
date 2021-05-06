package chess.service;

import chess.controller.dto.GameStatusDto;
import chess.controller.dto.MoveDto;
import chess.dao.CommandDao;
import chess.domain.game.BoardFactory;
import chess.domain.game.Game;
import chess.domain.location.Position;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChessService {
    private final CommandDao commandDao;

    public ChessService(CommandDao commandDao) {
        this.commandDao = commandDao;
    }

    public void move(Long gameId, String from, String to) {
        Game game = loadGame(gameId);
        game.move(Position.from(from), Position.from(to));
        commandDao.insert(gameId, from, to);
    }

    public GameStatusDto load(Long gameId) {
        Game game = loadGame(gameId);
        return new GameStatusDto(game);
    }

    private Game loadGame(Long gameId) {
        Game game = new Game(BoardFactory.create());
        List<MoveDto> moves = commandDao.findAllCommandOf(gameId);
        for (MoveDto move : moves) {
            game.move(Position.from(move.getFrom()), Position.from(move.getTo()));
        }
        return game;
    }
}