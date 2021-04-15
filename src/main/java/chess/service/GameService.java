package chess.service;

import chess.controller.dto.GameInfoDto;
import chess.domain.ChessGame;
import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.service.dao.GameDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameDao gameDao;

    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public void create(final Long roomId) {
        final ChessGame chessGame = ChessGame.initNew();
        gameDao.save(roomId, chessGame.turn(), chessGame.board());
    }

    public void delete(final Long roomId) {
        gameDao.delete(roomId);
    }

    public List<String> show(final Long roomId, final Position source) {
        final ChessGame chessGame = gameDao.load(roomId);
        return chessGame.reachablePositions(source);
    }

    public void move(final Long roomId, final Position source, final Position target) {
        final ChessGame chessGame = gameDao.load(roomId);
        chessGame.move(source, target);
        gameDao.update(roomId, chessGame.turn(), chessGame.board());
    }

    public boolean isGameEnd(final Long roomId) {
        final ChessGame chessGame = gameDao.load(roomId);
        return chessGame.isGameEnd();
    }

    public GameInfoDto gameInfo(final Long roomId) {
        final ChessGame chessGame = gameDao.load(roomId);
        return new GameInfoDto(chessGame.board(), chessGame.score(Owner.BLACK), chessGame.score(Owner.WHITE));
    }

    public List<Owner> winner(final Long roomId) {
        final ChessGame chessGame = gameDao.load(roomId);
        return chessGame.winner();
    }
}
