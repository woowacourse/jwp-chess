package chess.service;

import chess.dao.ChessGameDAO;
import chess.dao.MovementDAO;
import chess.domain.board.ChessGame;
import chess.domain.piece.property.Team;
import chess.domain.position.Movement;
import chess.domain.position.Position;
import chess.dto.GameCreationRequest;
import chess.dto.GameRoomResponse;
import chess.dto.MoveRequest;
import chess.exception.InvalidPasswordException;
import chess.exception.RunningGameDeletionException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final ChessGameDAO chessGameDAO;
    private final MovementDAO movementDAO;

    public ChessService(final ChessGameDAO chessGameDAO, final MovementDAO movementDAO) {
        this.chessGameDAO = chessGameDAO;
        this.movementDAO = movementDAO;
    }

    public long addChessGame(final GameCreationRequest gameCreationRequest) {
        return chessGameDAO.addGame(gameCreationRequest);
    }

    public ChessGame loadSavedGame(final long gameId) {
        List<Movement> movementByGameId = movementDAO.findMovementByGameId(gameId);
        ChessGame chessGame = chessGameDAO.findGameById(gameId);
        for (Movement movement : movementByGameId) {
            chessGame.execute(movement);
        }
        return chessGame;
    }

    public ChessGame movePiece(final long gameId, final MoveRequest moveRequest) {
        final ChessGame chessGame = loadSavedGame(gameId);
        validateCurrentTurn(chessGame, Team.valueOf(moveRequest.getTeam()));
        move(chessGame, new Movement(Position.of(moveRequest.getSource()), Position.of(moveRequest.getTarget()),
                chessGame.getId(), Team.valueOf(moveRequest.getTeam())));
        if (chessGame.isKingDied()) {
            chessGameDAO.updateGameEnd(gameId);
        }
        return chessGame;
    }

    private void validateCurrentTurn(final ChessGame chessGame, final Team team) {
        if (!chessGame.getChessBoard().getCurrentTurn().equals(team)) {
            throw new IllegalArgumentException("플레이어의 턴이 아닙니다");
        }
    }

    private void move(final ChessGame chessGame, final Movement movement) {
        chessGame.execute(movement);
        int insertedRowCount = movementDAO.addMoveCommand(movement);

        if (insertedRowCount == 0) {
            throw new IllegalArgumentException("플레이어의 턴이 아닙니다");
        }
    }

    public List<ChessGame> getGames() {
        return chessGameDAO.findAllGames();
    }

    public GameRoomResponse findGameById(long id) {
        return GameRoomResponse.from(chessGameDAO.findGameById(id));
    }

    public void deleteGame(final long gameId, final String password) {
        ChessGame chessGame = chessGameDAO.findGameById(gameId);
        if (!chessGame.isPasswordMatch(password)) {
            throw new InvalidPasswordException();
        }
        if (!chessGame.isEnd()) {
            throw new RunningGameDeletionException();
        }
        chessGameDAO.deleteGame(gameId);
    }
}
