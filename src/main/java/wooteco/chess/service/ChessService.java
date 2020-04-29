package wooteco.chess.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.dao.GameInfoDAO;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.gameinfo.GameInfo;
import wooteco.chess.domain.player.User;
import wooteco.chess.dto.LineDto;
import wooteco.chess.dto.RowsDtoConverter;
import wooteco.chess.repository.GameInfoRepository;
import wooteco.chess.repository.UserRepository;

@Service
public class ChessService {

    @Autowired
    private GameInfoRepository gameInfoRepository;

    @Autowired
    private UserRepository userRepository;
    private GameInfoDAO gameInfoDAO;
    private Map<User, GameInfo> games;

    public ChessService(GameInfoDAO gameInfoDAO) {
        this.gameInfoDAO = gameInfoDAO;
        games = new HashMap<>();
    }

    public GameInfo findByUserName(User blackUser) throws SQLException {
        if (!gameInfoDAO.findGameInfoByUser(blackUser).isPresent()) {
            userRepository.save(blackUser);
            gameInfoDAO.addGameInfo(BoardFactory.createInitialBoard(), blackUser, 0);
        }
        Board board = gameInfoDAO.findGameInfoByUser(blackUser)
                .orElseGet(BoardFactory::createInitialBoard);
        int turn = gameInfoDAO.findTurnByUser(blackUser)
                .orElseGet(() -> 0);
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

    public void save(User blackUser) throws SQLException {
        GameInfo gameInfo = games.get(blackUser);
        gameInfoDAO.saveGameInfoByUserName(gameInfo.getBoard(), blackUser, gameInfo.getStatus());
        games.remove(blackUser);
    }

    public void delete(User blackUser) throws SQLException {
        gameInfoDAO.deleteGameInfoByUser(blackUser);
        userRepository.delete(blackUser);
        games.remove(blackUser);
    }

    public List<String> searchPath(User blackUser, String sourceInput) {
        return games.get(blackUser)
                .searchPath(sourceInput)
                .stream()
                .map(Position::getName)
                .collect(Collectors.toList());
    }

    public List<LineDto> getEmptyRowsDto() {
        return RowsDtoConverter.convertFrom(BoardFactory.EMPTY_BOARD);
    }

    public List<LineDto> getRowsDto(User blackUser) throws SQLException {
        GameInfo gameInfo = findByUserName(blackUser);
        return RowsDtoConverter.convertFrom(gameInfo.getBoard());
    }

    public int getTurn(User user) {
        return games.get(user)
                .getStatus()
                .getTurn();
    }

    public double calculateWhiteScore(User user) {
        return games.get(user)
                .getWhiteScore()
                .getScore();
    }

    public double calculateBlackScore(User user) {
        return games.get(user)
                .getBlackScore()
                .getScore();
    }

    public boolean checkGameNotFinished(User user) {
        return games.get(user)
                .getStatus()
                .isNotFinished();
    }

    public Board getBoard(User user) {
        return games.get(user)
                .getBoard();
    }
}
