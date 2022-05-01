package chess.service;

import chess.domain.game.Movement;
import chess.domain.game.dao.MovementDAO;
import chess.domain.game.dto.MoveRequest;
import chess.domain.gameRoom.ChessGame;
import chess.domain.gameRoom.dao.ChessGameRoomDAO;
import chess.domain.gameRoom.dto.ChessGameRoomPassInfoResponse;
import chess.domain.gameRoom.dto.ChessGameRoomShowInfoResponse;
import chess.domain.piece.property.Team;
import chess.domain.position.Position;
import chess.exception.InvalidDeleteFailException;
import chess.exception.InvalidMoveException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChessService {

    private final ChessGameRoomDAO chessGameDAO;
    private final MovementDAO movementDAO;

    public ChessService(final ChessGameRoomDAO chessGameDAO, final MovementDAO movementDAO) {
        this.chessGameDAO = chessGameDAO;
        this.movementDAO = movementDAO;
    }

    public String addChessGame(final String gameName, final String password) {
        ChessGame chessGame = ChessGame.initChessGame();
        chessGame.setName(gameName);
        chessGame.setPassword(password);

        return chessGameDAO.addGame(chessGame);
    }

    @Transactional(readOnly = true)
    public ChessGame getChessGame(final String gameId) {
        List<Movement> movementByGameId = movementDAO.findMovementByGameId(gameId);
        ChessGame chessGame = ChessGame.initChessGame();
        for (Movement movement : movementByGameId) {
            chessGame.execute(movement);
        }
        chessGame.setId(gameId);
        return chessGame;
    }

    public ChessGame movePiece(final String gameId, final MoveRequest moveRequest) {
        final ChessGame chessGame = getChessGame(gameId);
        final String source = moveRequest.getSource();
        final String target = moveRequest.getTarget();
        final Team team = moveRequest.getTeam();

        validateCurrentTurn(chessGame, team);
        move(chessGame, new Movement(Position.of(source), Position.of(target)), team);
        if (chessGame.isKingDied()) {
            chessGameDAO.updateGameEnd(gameId);
        }
        return chessGame;
    }

    private void validateCurrentTurn(final ChessGame chessGame, final Team team) {
        if (!chessGame.getChessBoard().getCurrentTurn().equals(team)) {
            throw new InvalidMoveException("[ERROR] 플레이어의 턴이 아닙니다");
        }
    }

    private void move(final ChessGame chessGame, final Movement movement, final Team team) {
        chessGame.execute(movement);
        movement.setGameId(chessGame.getId());
        movement.setTeam(team);
        movementDAO.addMoveCommand(movement);
    }

    @Transactional(readOnly = true)
    public List<ChessGameRoomShowInfoResponse> getGames() {
        return chessGameDAO.findActiveGames();
    }

    @Transactional(readOnly = true)
    public ChessGameRoomShowInfoResponse findGameById(String id) {
        return chessGameDAO.findShowGameById(id);
    }

    public void deleteGameByIdAndPassword(String id, String password) {
        ChessGameRoomPassInfoResponse chessGameRoom = chessGameDAO.findPassGameById(id);
        if (!chessGameRoom.checkEqualPassword(password)){
            throw new InvalidDeleteFailException("[ERROR] DELETE를 실패하였습니다.");
        }
        chessGameDAO.deleteGameByIdAndPassword(id, password);
    }
}
