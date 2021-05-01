package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.dto.GameDto;
import chess.exception.InvalidUserException;
import chess.service.dao.GameDao;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GameService {

    private final GameDao gameDao;

    public GameService(final GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public List<String> reachable(final long roomId, final Position source, final Owner owner) {
        try {
            final ChessGame chessGame = gameDao.load(roomId);
            validateTurn(chessGame, owner);
            validateOwner(chessGame, source, owner);
            return chessGame.reachablePositions(source);
        } catch (InvalidUserException exception) {
            return Collections.EMPTY_LIST;
        }
    }

    private void validateOwner(final ChessGame chessGame, final Position source, final Owner player) {
        final Owner pieceOwner = chessGame.ownerOf(source);
        if (pieceOwner.isSameTeam(player)) {
            return;
        }
        throw new InvalidUserException("사용자의 기물이 아닙니다.");
    }

    private void validateTurn(final ChessGame chessGame, final Owner owner) {
        if (chessGame.isTurn(owner)) {
            return;
        }
        throw new InvalidUserException("현재 사용자의 차례가 아닙니다.");
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

    public GameDto gameInfo(final Long roomId) {
        final ChessGame chessGame = gameDao.load(roomId);
        return new GameDto(chessGame.board(), chessGame.score(Owner.BLACK), chessGame.score(Owner.WHITE));
    }

    public List<Owner> winner(final Long roomId) {
        final ChessGame chessGame = gameDao.load(roomId);
        return chessGame.winner();
    }
}
