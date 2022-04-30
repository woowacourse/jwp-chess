package chess.service;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.dao.GameStatusDao;
import chess.dao.TurnDao;
import chess.domain.ChessGame;
import chess.domain.GameStatus;
import chess.domain.board.Board;
import chess.domain.board.Result;
import chess.domain.board.strategy.BoardGenerationStrategy;
import chess.domain.board.strategy.CustomBoardStrategy;
import chess.domain.board.strategy.WebBasicBoardStrategy;
import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceConvertor;
import chess.domain.piece.Team;
import chess.domain.position.Position;
import chess.dto.GameDto;
import chess.dto.GameStatusDto;
import chess.dto.ScoreDto;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    public static final String WIN_MESSAGE = "승리 팀은 : %s 입니다.";

    private final BoardDao boardDao;
    private final TurnDao turnDao;
    private final GameStatusDao gameStatusDao;
    private final GameDao gameDao;

    @Autowired
    public ChessGameService(BoardDao boardDao, TurnDao turnDao, GameStatusDao gameStatusDao, GameDao gameDao) {
        this.boardDao = boardDao;
        this.turnDao = turnDao;
        this.gameStatusDao = gameStatusDao;
        this.gameDao = gameDao;
    }

    public int create(GameDto gameDto) {
        return gameDao.create(gameDto.getRoomTitle(), gameDto.getPassword());
    }

    public List<GameDto> find() {
        return gameDao.find();
    }

    public void delete(int id, String password) {
        boolean isDeleted = gameDao.delete(id, password);
        if (!isDeleted) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void init(int gameId) {
        try {
            turnDao.getTurn(gameId);
        } catch (Exception e) {
            turnDao.init(gameId);
            gameStatusDao.init(gameId);
        }
    }

    public GameStatusDto startChessGame(BoardGenerationStrategy strategy, int gameId) {
        if (gameStatusDao.getStatus(gameId).equals(GameStatus.PLAYING.toString())) {
            return loadChessGame(gameId);
        }
        ChessGame chessGame = new ChessGame();
        chessGame.startGame(strategy);
        boardDao.init(chessGame.toMap(), gameId);
        gameStatusDao.update(gameStatusDao.getStatus(gameId), chessGame.getGameStatus().toString(), gameId);
        return GameStatusDto.of(chessGame);
    }

    public GameStatusDto loadChessGame(int gameId) {
        ChessGame chessGame = createCustomChessGame(gameId);
        return GameStatusDto.of(chessGame);
    }

    public ScoreDto createScore(int gameId) {
        Board board = createCustomBoard(boardDao.getBoard(gameId));
        Result result = board.createResult();
        return ScoreDto.of(result);
    }

    public GameStatusDto move(String from, String to, int gameId) {
        checkReady(gameId);

        ChessGame chessGame = createCustomChessGame(gameId);
        moveAndUpdateBoard(from, to, chessGame, gameId);
        chessGame.checkGameStatus();

        turnDao.update(turnDao.getTurn(gameId), chessGame.getTurn().toString());
        gameStatusDao.update(gameStatusDao.getStatus(gameId), chessGame.getGameStatus().toString(), gameId);

        return GameStatusDto.of(chessGame);
    }

    private void checkReady(int gameId) {
        GameStatus gameStatus = GameStatus.of(gameStatusDao.getStatus(gameId));
        if (gameStatus.isReady()) {
            throw new IllegalArgumentException("체스 게임을 시작해야 합니다.");
        }
    }

    private ChessGame createCustomChessGame(int gameId) {
        return new ChessGame(Team.of(turnDao.getTurn(gameId)), GameStatus.of(gameStatusDao.getStatus(gameId)),
                createCustomBoard(boardDao.getBoard(gameId)));
    }

    private Board createCustomBoard(Map<String, String> data) {
        Board board = new Board();
        board.initBoard(createStrategy(data));
        return board;
    }

    private CustomBoardStrategy createStrategy(Map<String, String> data) {
        Map<Position, Piece> board = data.entrySet()
                .stream()
                .collect(Collectors.toMap(m -> new Position(m.getKey()), m -> PieceConvertor.of(m.getValue())));

        CustomBoardStrategy strategy = new CustomBoardStrategy();
        strategy.put(board);
        return strategy;
    }

    private void moveAndUpdateBoard(String fromData, String toData, ChessGame chessGame, int gameId) {
        Position from = new Position(fromData);
        Position to = new Position(toData);
        chessGame.move(from, to);
        boardDao.update(from.toString(), new Blank().toString(), gameId);
        boardDao.update(to.toString(), chessGame.takePieceByPosition(to).toString(), gameId);
    }

    public ScoreDto end(int gameId) {
        ChessGame chessGame = createCustomChessGame(gameId);
        checkReady(gameId);
        Result result = chessGame.stop();
        return createEndScore(result, gameId);
    }

    private ScoreDto createEndScore(Result result, int gameId) {
        ScoreDto scoreDto = null;
        if (gameStatusDao.getStatus(gameId).equals(GameStatus.CHECK_MATE.toString())) {
            scoreDto = new ScoreDto(String.format(WIN_MESSAGE, Team.of(turnDao.getTurn(gameId)).change()));
        }
        resetBoard();
        turnDao.reset(1);
        gameStatusDao.reset(1);
        return selectScoreDto(result, scoreDto);
    }

    private void resetBoard() {
        Board board = new Board();
        board.initBoard(new WebBasicBoardStrategy());
        boardDao.reset(board.toMap(), 1);
    }

    private ScoreDto selectScoreDto(Result result, ScoreDto scoreDto) {
        if (Objects.isNull(scoreDto)) {
            return ScoreDto.of(result);
        }
        return scoreDto;
    }
}
