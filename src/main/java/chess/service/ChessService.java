package chess.service;

import chess.domain.board.ChessBoardGenerator;
import chess.domain.piece.property.Team;
import chess.domain.position.Position;
import chess.dao.ChessGame;
import chess.dao.ChessGameDAO;
import chess.dao.Movement;
import chess.dao.MovementDAO;
import chess.dto.ChessGameRoomInfoDTO;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public final class ChessService {

//    private static final ChessGameDAO CHESS_GAME_DAO = new ChessGameDAO();
//    private static final MovementDAO MOVEMENT_DAO = new MovementDAO();
    private final ChessGameDAO chessGameDAO;
    private final MovementDAO movementDAO;

    public ChessService(final ChessGameDAO chessGameDAO, final MovementDAO movementDAO) {
        this.chessGameDAO = chessGameDAO;
        this.movementDAO = movementDAO;
    }

    public String addChessGame(final String gameName) throws SQLException {
        ChessGame chessGame = new ChessGame(new ChessBoardGenerator());
        chessGame.setName(gameName);

        return chessGameDAO.addGame(chessGame);
    }

    public ChessGame getChessGamePlayed(final String gameId) throws SQLException {
        List<Movement> movementByGameId = movementDAO.findMovementByGameId(gameId);
        ChessGame chessGame = ChessGame.initChessGame();
        for (Movement movement : movementByGameId) {
            chessGame.execute(movement);
        }
        chessGame.setId(gameId);
        return chessGame;
    }

    public ChessGame movePiece(final String gameId, final String source, final String target, final Team team)
            throws SQLException {
        final ChessGame chessGame = getChessGamePlayed(gameId);
        validateCurrentTurn(chessGame, team);
        move(chessGame, new Movement(Position.of(source), Position.of(target)), team);
        if (chessGame.isGameSet()){
            chessGameDAO.updateGameEnd(gameId);
        }
        return chessGame;
    }

    private void validateCurrentTurn(final ChessGame chessGame, final Team team) {
        if (!chessGame.getChessBoard().getCurrentTurn().equals(team)) {
            throw new IllegalArgumentException("플레이어의 턴이 아닙니다");
        }
    }

    private void move(final ChessGame chessGame, final Movement movement, final Team team) throws SQLException {
        chessGame.execute(movement);
        movement.setGameId(chessGame.getId());
        movement.setTeam(team);
        int insertedRowCount = movementDAO.addMoveCommand(movement);

        if (insertedRowCount == 0) {
            throw new IllegalArgumentException("플레이어의 턴이 아닙니다");
        }
    }

    private Map<String, Object> result(final ChessGame chessGame) {
        Map<String, Object> model = new HashMap<>();
        model.put("winner", chessGame.getChessBoard().calculateWhoWinner().toString());

        return model;
    }

    public List<ChessGameRoomInfoDTO> getGames() throws SQLException {
        return chessGameDAO.findActiveGames();
    }

    public ChessGameRoomInfoDTO findGameById(String id) {
        return chessGameDAO.findGameById(id);
    }

    public Map<String, Object> getResult(ChessGame chessGame) throws SQLException {
        final Map<String, Object> model = new HashMap<>();
        chessGameDAO.updateGameEnd(chessGame.getId());
        model.put("gameResult", result(chessGame));
        model.put("isGameSet", Boolean.TRUE);

        return model;
    }
}
