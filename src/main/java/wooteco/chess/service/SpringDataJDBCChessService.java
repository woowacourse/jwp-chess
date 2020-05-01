package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.NormalStatus;
import wooteco.chess.domain.position.MovingPosition;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.ChessGameDto;
import wooteco.chess.dto.MovablePositionsDto;
import wooteco.chess.dto.MoveStatusDto;
import wooteco.chess.entity.Game;
import wooteco.chess.entity.GameRepository;
import wooteco.chess.entity.History;
import wooteco.chess.entity.HistoryRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SpringDataJDBCChessService {
    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private GameRepository gameRepository;

    public void clearHistory() {
        historyRepository.deleteAll();
    }

    public ChessGameDto setBoard() {
        ChessGame chessGame = new ChessGame();

        load(chessGame);

        return new ChessGameDto(new BoardDto(chessGame.getPieces()), chessGame.getTurn(), chessGame.calculateScore(), NormalStatus.YES.isNormalStatus());
    }

    public ChessGameDto createGameBy(String gameName) {
        ChessGame chessGame = new ChessGame();
        Long gameId = save(gameName);
        return new ChessGameDto(gameId, new BoardDto(chessGame.getPieces()), chessGame.getTurn(),
                chessGame.calculateScore(), NormalStatus.YES.isNormalStatus());
    }

    private Long save(String gameName) {
        Game game = gameRepository.save(new Game(gameName, true));
        return game.getId();
    }

    public ChessGameDto setBoardBy(Long id) {
        ChessGame chessGame = new ChessGame();
        load(chessGame, id);
        return new ChessGameDto(new BoardDto(chessGame.getPieces()), chessGame.getTurn(),
                chessGame.calculateScore(), NormalStatus.YES.isNormalStatus());
    }

    private void load(ChessGame chessGame, Long id) {
        List<MovingPosition> histories = selectAllHistoryBy(id);
        for (MovingPosition movingPosition : histories) {
            chessGame.move(movingPosition);
        }
    }

    private List<MovingPosition> selectAllHistoryBy(Long id) {
        Game game = gameRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("없는 게임 id 입니다."));
        Set<History> histories = game.getHistories();

        return Collections.unmodifiableList(histories.stream()
                .map(h -> new MovingPosition(h.getStart(), h.getEnd()))
                .collect(Collectors.toList()));
    }

    private void load(ChessGame chessGame) {
        List<MovingPosition> histories = selectAllHistory();

        for (MovingPosition movingPosition : histories) {
            chessGame.move(movingPosition);
        }
    }

    private List<MovingPosition> selectAllHistory() {
        return Collections.unmodifiableList(historyRepository.findAll().stream()
                .map(history -> new MovingPosition(history.getStart(), history.getEnd()))
                .collect(Collectors.toList()));
    }

    public MovablePositionsDto findMovablePositions(String source) {
        ChessGame chessGame = new ChessGame();
        load(chessGame);

        List<String> movablePositionNames = chessGame.findMovablePositionNames(source);

        return new MovablePositionsDto(movablePositionNames, source);

    }

    public MoveStatusDto checkMovable(MovingPosition movingPosition) {
        try {
            ChessGame chessGame = new ChessGame();

            load(chessGame);
            chessGame.move(movingPosition);
            return new MoveStatusDto(NormalStatus.YES.isNormalStatus());
        } catch (IllegalArgumentException | UnsupportedOperationException | NullPointerException e) {
            return new MoveStatusDto(NormalStatus.NO.isNormalStatus(), e.getMessage());
        }
    }

    public MoveStatusDto move(MovingPosition movingPosition) {
        ChessGame chessGame = new ChessGame();
        load(chessGame);
        chessGame.move(movingPosition);

        MoveStatusDto moveStatusDto = new MoveStatusDto(NormalStatus.YES.isNormalStatus());

        if (chessGame.isKingDead()) {
            moveStatusDto = new MoveStatusDto(NormalStatus.YES.isNormalStatus(), chessGame.getAliveKingColor());
        }

        insertHistory(movingPosition);

        return moveStatusDto;
    }

    private void insertHistory(MovingPosition movingPosition) {
        History history = new History(movingPosition.getStart(), movingPosition.getEnd());
        historyRepository.save(history);
    }
}
