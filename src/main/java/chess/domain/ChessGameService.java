package chess.domain;

import chess.dao.BoardDao;
import chess.dao.GameStatusDao;
import chess.dao.RoomDao;
import chess.dao.TurnDao;
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
import chess.dto.BoardDto;
import chess.dto.GameStatusDto;
import chess.dto.RoomResponseDto;
import chess.dto.ScoreDto;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChessGameService {

    public static final String WIN_MESSAGE = "승리 팀은 : %s 입니다.";

    private final BoardDao boardDao;
    private final TurnDao turnDao;
    private final GameStatusDao gameStatusDao;


    public ChessGameService(BoardDao boardDao, TurnDao turnDao, GameStatusDao gameStatusDao
    ) {
        this.boardDao = boardDao;
        this.turnDao = turnDao;
        this.gameStatusDao = gameStatusDao;
    }

    public int createGame( int roomId) {
        turnDao.create(Team.WHITE, roomId);
        gameStatusDao.create(GameStatus.READY, roomId);
        boardDao.create(createInitBoard(), roomId);
        return roomId;
    }

    private Map<String, String> createInitBoard() {
        Board board = new Board();
        board.initBoard(new WebBasicBoardStrategy());
        return board.toMap();
    }

    public GameStatusDto startChessGame(BoardGenerationStrategy strategy, int roomId) {
        if (gameStatusDao.getStatus(roomId).equals(GameStatus.PLAYING.toString())) {
            return loadChessGame(roomId);
        }
        ChessGame chessGame = new ChessGame();
        chessGame.startGame(strategy);
        gameStatusDao.update(gameStatusDao.getStatus(roomId), chessGame.getGameStatus().toString(), roomId);
        return GameStatusDto.of(chessGame);
    }

    public GameStatusDto loadChessGame(int roomId) {
        ChessGame chessGame = createCustomChessGame(roomId);
        return GameStatusDto.of(chessGame);
    }

    public ScoreDto createScore(int roomId) {
        Board board = createCustomBoard(toMap(boardDao.getBoard(roomId)));
        Result result = board.createResult();
        return ScoreDto.of(result);
    }

    private Map<String, String> toMap(List<BoardDto> data) {
        return data.stream().collect(Collectors.toMap(BoardDto::getPosition, BoardDto::getPiece));
    }

    public GameStatusDto move(String from, String to, int roomId) {
        checkReady(roomId);

        ChessGame chessGame = createCustomChessGame(roomId);
        moveAndUpdateBoard(from, to, chessGame, roomId);
        chessGame.checkGameStatus();

        turnDao.update(turnDao.getTurn(roomId), chessGame.getTurn().toString(), roomId);
        gameStatusDao.update(gameStatusDao.getStatus(roomId), chessGame.getGameStatus().toString(), roomId);

        return GameStatusDto.of(chessGame);
    }

    private void checkReady(int roomId) {
        GameStatus gameStatus = GameStatus.of(gameStatusDao.getStatus(roomId));
        if (gameStatus.isReady()) {
            throw new IllegalArgumentException("체스 게임을 시작해야 합니다.");
        }
    }

    private ChessGame createCustomChessGame(int roomId) {
        return new ChessGame(Team.of(turnDao.getTurn(roomId)), GameStatus.of(gameStatusDao.getStatus(roomId)),
                createCustomBoard(toMap(boardDao.getBoard(roomId))));
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

    private void moveAndUpdateBoard(String fromData, String toData, ChessGame chessGame, int roomId) {
        Position from = new Position(fromData);
        Position to = new Position(toData);
        chessGame.move(from, to);
        boardDao.update(from.toString(), new Blank().toString(), roomId);
        boardDao.update(to.toString(), chessGame.takePieceByPosition(to).toString(), roomId);
    }

    public ScoreDto end(int id) {
        ChessGame chessGame = createCustomChessGame(id);
        checkReady(id);
        Result result = chessGame.stop();
        return createEndScore(result, id);
    }

    private ScoreDto createEndScore(Result result, int roomId) {
        ScoreDto scoreDto = null;
        if (gameStatusDao.getStatus(roomId).equals(GameStatus.CHECK_MATE.toString())) {
            scoreDto = new ScoreDto(String.format(WIN_MESSAGE, Team.of(turnDao.getTurn(roomId)).change()));
        }
        resetBoard(roomId);
        turnDao.reset(Team.WHITE, roomId);
        gameStatusDao.reset(GameStatus.READY, roomId);
        return selectScoreDto(result, scoreDto);
    }

    private void resetBoard(int roomId) {
        Board board = new Board();
        board.initBoard(new WebBasicBoardStrategy());
        boardDao.reset(board.toMap(), roomId);
    }

    private ScoreDto selectScoreDto(Result result, ScoreDto scoreDto) {
        if (Objects.isNull(scoreDto)) {
            return ScoreDto.of(result);
        }
        return scoreDto;
    }
}
