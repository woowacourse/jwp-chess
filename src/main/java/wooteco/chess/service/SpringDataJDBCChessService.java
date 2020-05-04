package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.NormalStatus;
import wooteco.chess.domain.position.MovingPosition;
import wooteco.chess.dto.*;
import wooteco.chess.entity.Game;
import wooteco.chess.entity.GameRepository;
import wooteco.chess.entity.History;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpringDataJDBCChessService {
    @Autowired
    private GameRepository gameRepository;

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

    public MovablePositionsDto findMovablePositions(Long id, String source) {
        ChessGame chessGame = new ChessGame();
        load(chessGame, id);

        List<String> movablePositionNames = chessGame.findMovablePositionNames(source);

        return new MovablePositionsDto(movablePositionNames, source);

    }

    public MoveStatusDto checkMovable(Long id, MovingPosition movingPosition) {
        try {
            ChessGame chessGame = new ChessGame();

            load(chessGame, id);
            chessGame.move(movingPosition);
            return new MoveStatusDto(NormalStatus.YES.isNormalStatus());
        } catch (IllegalArgumentException | UnsupportedOperationException | NullPointerException e) {
            return new MoveStatusDto(NormalStatus.NO.isNormalStatus(), e.getMessage());
        }
    }

    public MoveStatusDto move(Long id, MovingPosition movingPosition) {
        ChessGame chessGame = new ChessGame();
        load(chessGame, id);
        chessGame.move(movingPosition);

        MoveStatusDto moveStatusDto = new MoveStatusDto(NormalStatus.YES.isNormalStatus());

        if (chessGame.isKingDead()) {
            updateCanContinueToFalse(id);
            moveStatusDto = new MoveStatusDto(NormalStatus.YES.isNormalStatus(), chessGame.getAliveKingColor());
        }

        insertHistory(id, movingPosition);

        return moveStatusDto;
    }

    private void updateCanContinueToFalse(Long id) {
        Game game = gameRepository.findById(id).get(); // TODO: 2020/05/01 예외 처리 수정
        gameRepository.save(new Game(game.getId(), game.getName(), false));
    }

    private void insertHistory(Long id, MovingPosition movingPosition) {
        Game game = gameRepository.findById(id).get(); // TODO: 2020/05/01 예외 처리 생각하기
        History history = new History(movingPosition.getStart(), movingPosition.getEnd());
        game.addHistory(history);
        gameRepository.save(game);
    }

    public GamesDto selectAvailableGames() {
        Map<Long, String> games = gameRepository.findAvailableGames().stream()
                .collect(Collectors.toMap(game -> game.getId(), game -> game.getName(),
                        (e1, e2) -> e1, LinkedHashMap::new));
        return new GamesDto(games);
    }
}
