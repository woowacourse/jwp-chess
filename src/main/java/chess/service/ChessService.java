package chess.service;

import chess.dao.ChessGameDAO;
import chess.dao.MovementDAO;
import chess.domain.board.ChessGame;
import chess.domain.piece.property.Team;
import chess.domain.position.Movement;
import chess.domain.position.Position;
import chess.dto.ChessGameRoomInfoDTO;
import chess.dto.GameCreationDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public final class ChessService {

    private final ChessGameDAO chessGameDAO;
    private final MovementDAO movementDAO;

    public ChessService(final ChessGameDAO chessGameDAO, final MovementDAO movementDAO) {
        this.chessGameDAO = chessGameDAO;
        this.movementDAO = movementDAO;
    }

    public long addChessGame(final GameCreationDTO gameCreationDTO) {
        return chessGameDAO.addGame(gameCreationDTO);
    }

    public ChessGame getChessGamePlayed(final String gameId) {
        List<Movement> movementByGameId = movementDAO.findMovementByGameId(gameId);
        ChessGame chessGame = chessGameDAO.findGameById(gameId);
        for (Movement movement : movementByGameId) {
            chessGame.execute(movement);
        }
        return chessGame;
    }

    public ChessGame movePiece(final String gameId, final String source, final String target, final Team team) {
        final ChessGame chessGame = getChessGamePlayed(gameId);
        validateCurrentTurn(chessGame, team);
        move(chessGame, new Movement(Position.of(source), Position.of(target)), team);
        if (chessGame.isKingDied()){
            chessGameDAO.updateGameEnd(gameId);
        }
        return chessGame;
    }

    private void validateCurrentTurn(final ChessGame chessGame, final Team team) {
        if (!chessGame.getChessBoard().getCurrentTurn().equals(team)) {
            throw new IllegalArgumentException("플레이어의 턴이 아닙니다");
        }
    }

    private void move(final ChessGame chessGame, final Movement movement, final Team team) {
        chessGame.execute(movement);
        movement.setGameId(chessGame.getId());
        movement.setTeam(team);
        int insertedRowCount = movementDAO.addMoveCommand(movement);

        if (insertedRowCount == 0) {
            throw new IllegalArgumentException("플레이어의 턴이 아닙니다");
        }
    }

    public List<ChessGameRoomInfoDTO> getGames() {
        return chessGameDAO.findAllGames();
    }

    public ChessGameRoomInfoDTO findGameById(String id) {
        return ChessGameRoomInfoDTO.from(chessGameDAO.findGameById(id));
    }

    public void deleteGame(final String gameId, final String password) {
        ChessGame chessGame = getChessGamePlayed(gameId);
        if (!chessGame.isMatched(password)) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }
        if (!chessGame.isEnd()) {
            throw new IllegalArgumentException("진행중인 게임은 삭제할 수 없습니다.");
        }
        chessGameDAO.deleteGame(gameId);
    }
}
