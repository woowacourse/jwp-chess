package wooteco.chess.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wooteco.chess.dao.GamInfoDAO;
import wooteco.chess.dao.UserDAO;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.gameinfo.GameInfo;
import wooteco.chess.domain.player.User;
import wooteco.chess.dto.LineDto;
import wooteco.chess.dto.RowsDtoConverter;
import wooteco.chess.util.DBConnector;

public class ChessService {

    private GamInfoDAO gamInfoDAO;
    private Map<User, GameInfo> games;
    private DBConnector dbConnector;

    public ChessService() {
        games = new HashMap<>();
    }

    public GameInfo findByUserName(User blackUser, User whiteUser) throws SQLException {
        dbConnector = new DBConnector();
        gamInfoDAO = new GamInfoDAO(dbConnector);
        Board board;
        if (!gamInfoDAO.findGameInfoByUser(blackUser, whiteUser).isPresent()) {
            UserDAO userDAO = new UserDAO(dbConnector);
            userDAO.addUser(blackUser);
            userDAO.addUser(whiteUser);
            gamInfoDAO.addGameInfo(BoardFactory.createInitialBoard(), blackUser, whiteUser, 0);
        }
        board = gamInfoDAO.findGameInfoByUser(blackUser, whiteUser)
                .orElse(BoardFactory.createInitialBoard());
        int turn = gamInfoDAO.findTurnByUser(blackUser, whiteUser)
                .orElse(0);
        GameInfo gameInfo = GameInfo.from(board, turn);
        games.put(blackUser, gameInfo);
        return gameInfo;
    }

    public GameInfo move(User blackUser, String source, String target) {
        GameInfo gameInfo = games.get(blackUser)
                .move(source, target);
        games.put(blackUser, gameInfo);
        return gameInfo;
    }

    public void save(User blackUser, User whiteUser) throws SQLException {
        GameInfo gameInfo = games.get(blackUser);
        gamInfoDAO.saveGameInfoByUserName(gameInfo.getBoard(), blackUser, whiteUser, gameInfo.getTurn());
        games.remove(blackUser);
    }

    public void delete(User blackUser, User whiteUser) throws SQLException {
        gamInfoDAO.deleteGameInfoByUser(blackUser, whiteUser);
        UserDAO userDAO = new UserDAO(dbConnector);
        userDAO.deleteUserByUserName(blackUser.getName());
        userDAO.deleteUserByUserName(whiteUser.getName());
        games.remove(blackUser);
        dbConnector.closeConnection();
    }

    public List<String> searchPath(User blackUser, String sourceInput) {
        return games.get(blackUser).searchPath(sourceInput);
    }

    public List<LineDto> getEmptyRowsDto() {
        return RowsDtoConverter.convertFrom(BoardFactory.EMPTY_BOARD);
    }

    public List<LineDto> getRowsDto(User blackUser, User whiteUser) throws SQLException {
        GameInfo gameInfo = findByUserName(blackUser, whiteUser);
        return RowsDtoConverter.convertFrom(gameInfo.getBoard());
    }

    public int getTurn(User blackUser) {
        return games.get(blackUser)
                .getTurn();
    }

    public double calculateWhiteScore(User blackUser) {
        return games.get(blackUser).getWhiteScore();
    }

    public double calculateBlackScore(User blackUser) {
        return games.get(blackUser).getBlackScore();
    }

    public boolean checkGameNotFinished(User blackUser) {
        return games.get(blackUser)
                .isNotFinished();
    }

    public Board getBoard(User blackUser) {
        return games.get(blackUser).getBoard();
    }
}
