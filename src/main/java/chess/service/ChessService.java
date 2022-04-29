package chess.service;

import chess.domain.game.dto.MoveDTO;
import chess.domain.gameRoom.dto.ChessGameRoomPassInfoDTO;
import chess.domain.piece.property.Team;
import chess.domain.position.Position;
import chess.domain.gameRoom.ChessGame;
import chess.domain.gameRoom.dao.ChessGameRoomDAO;
import chess.domain.game.Movement;
import chess.domain.game.dao.MovementDAO;
import chess.domain.gameRoom.dto.ChessGameRoomShowInfoDTO;

import java.util.List;
import chess.exception.InvalidDBFailException;
import chess.exception.InvalidMoveException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ChessGame getChessGamePlayed(final String gameId) {
        List<Movement> movementByGameId = movementDAO.findMovementByGameId(gameId);
        ChessGame chessGame = ChessGame.initChessGame();
        for (Movement movement : movementByGameId) {
            chessGame.execute(movement);
        }
        chessGame.setId(gameId);
        return chessGame;
    }

    public ChessGame movePiece(final String gameId, final MoveDTO moveDTO) {
        final ChessGame chessGame = getChessGamePlayed(gameId);
        final String source = moveDTO.getSource();
        final String target = moveDTO.getTarget();
        final Team team = moveDTO.getTeam();

        validateCurrentTurn(chessGame, team);
        move(chessGame, new Movement(Position.of(source), Position.of(target)), team);
        if (chessGame.isGameSet()) {
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
        int successUpdate = movementDAO.addMoveCommand(movement);

        if (successUpdate == 0) {
            throw new InvalidDBFailException("[ERROR] DB UPDATE를 실패하였습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<ChessGameRoomShowInfoDTO> getGames() {
        return chessGameDAO.findActiveGames();
    }

    @Transactional(readOnly = true)
    public ChessGameRoomShowInfoDTO findGameById(String id) {
        return chessGameDAO.findShowGameById(id);
    }

    public void deleteGameByIdAndPassword(String id, String password) {
        ChessGameRoomPassInfoDTO chessGameRoom = chessGameDAO.findPassGameById(id);
        if (!chessGameRoom.getPassword().equals(password)){
            throw new InvalidDBFailException("[ERROR] DELETE를 실패하였습니다.");
        }
        chessGameDAO.deleteGameByIdAndPassword(id, password);
    }
}
