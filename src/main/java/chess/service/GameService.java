package chess.service;

import chess.controller.dto.GameInfoDto;
import chess.domain.ChessGame;
import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.domain.player.Turn;
import chess.service.dao.GameDao;
import chess.service.dao.RoomDao;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GameService {

    private final GameDao gameDao;

    // TODO :: roomDao 제거
    private final RoomDao roomDao;

    public GameService(final GameDao gameDao, final RoomDao roomDao) {
        this.gameDao = gameDao;
        this.roomDao = roomDao;
    }

    public void create(final long roomId) {
        final ChessGame chessGame = ChessGame.initNew();
        gameDao.save(roomId, chessGame.turn(), chessGame.board());
    }

    public void delete(final long roomId) {
        gameDao.delete(roomId);
    }

    public List<String> reachable(final long roomId, final Position source, final Owner owner) {
        try {
            final ChessGame chessGame = gameDao.load(roomId);
            chessGame.validateTurn(owner);
            return chessGame.reachablePositions(source);
        } catch (IllegalArgumentException exception) {
            return Collections.EMPTY_LIST;
        }
    }

    public void move(final long roomId, final Position source, final Position target) {
        final ChessGame chessGame = gameDao.load(roomId);
        chessGame.move(source, target);
        gameDao.update(roomId, chessGame.turn(), chessGame.board());
    }

    public boolean isGameEnd(final long roomId) {
        final ChessGame chessGame = gameDao.load(roomId);
        return chessGame.isGameEnd();
    }

    public GameInfoDto gameInfo(final long roomId) {
        final ChessGame chessGame = gameDao.load(roomId);
        return new GameInfoDto(chessGame.board(), chessGame.score(Owner.BLACK), chessGame.score(Owner.WHITE));
    }

    public List<Owner> winner(final long roomId) {
        final ChessGame chessGame = gameDao.load(roomId);
        return chessGame.winner();
    }
}
