package chess.service;

import chess.dao.PlayLogDao;
import chess.dao.RoomDao;
import chess.dao.UserDao;
import chess.domain.board.Board;
import chess.domain.board.Point;
import chess.domain.board.Team;
import chess.domain.chessgame.ChessGame;
import chess.domain.chessgame.ScoreBoard;
import chess.domain.chessgame.Turn;
import chess.domain.gamestate.GameState;
import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;
import chess.dto.web.PointDto;
import chess.dto.web.RoomDto;
import chess.dto.web.UsersInRoomDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SpringChessService implements ChessService {

    private final RoomDao roomDao;
    private final UserDao userDao;
    private final PlayLogDao playLogDao;

    public SpringChessService(RoomDao roomDao, UserDao userDao, PlayLogDao playLogDao) {
        this.roomDao = roomDao;
        this.userDao = userDao;
        this.playLogDao = playLogDao;
    }

    @Override
    public List<RoomDto> openedRooms() {
        return roomDao.openedRooms();
    }

    @Override
    public BoardDto latestBoard(String id) {
        return playLogDao.latestBoard(id);
    }

    @Override
    public UsersInRoomDto usersInRoom(String id) {
        return userDao.usersInRoom(id);
    }

    @Override
    public RoomDto create(RoomDto roomDto) {
        String blackName = roomDto.getBlack();
        String whiteName = roomDto.getWhite();

        if (blackName.equals(whiteName)) {
            throw new IllegalArgumentException("방 안에 같은 사용자가 2명 존재할 수 없습니다.");
        }

        userDao.insert(whiteName);
        userDao.insert(blackName);
        String roomId = roomDao.insert(roomDto);
        roomDto.setId(roomId);

        Board board = new Board();
        ChessGame chessGame = new ChessGame(board);

        playLogDao.insert(new BoardDto(board), new GameStatusDto(chessGame), roomId);
        return roomDto;
    }

    @Override
    public GameStatusDto gameStatus(String id) {
        Board board = boardFromDb(id);
        ChessGame chessGame = chessGameFromDb(board, id);
        return new GameStatusDto(chessGame);
    }

    @Override
    public BoardDto start(String id) {
        Board board = boardFromDb(id);
        ChessGame chessGame = chessGameFromDb(board, id);
        chessGame.start();
        BoardDto boardDto = new BoardDto(board);
        GameStatusDto gameStatusDto = new GameStatusDto(chessGame);
        playLogDao.insert(boardDto, gameStatusDto, id);
        return new BoardDto(board);
    }

    @Override
    public BoardDto exit(String id) {
        Board board = boardFromDb(id);
        ChessGame chessGame = chessGameFromDb(board, id);
        chessGame.end();
        BoardDto boardDto = new BoardDto(board);
        GameStatusDto gameStatusDto = new GameStatusDto(chessGame);
        playLogDao.insert(boardDto, gameStatusDto, id);
        return boardDto;
    }

    @Override
    public void close(String id) {
        roomDao.close(id);
    }

    @Override
    public List<PointDto> movablePoints(String id, String point) {
        Board board = boardFromDb(id);
        ChessGame chessGame = chessGameFromDb(board, id);
        List<Point> movablePoints = chessGame.movablePoints(Point.of(point));
        return movablePoints.stream()
            .map(PointDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public BoardDto move(String id, String source, String destination) {
        Board board = boardFromDb(id);
        ChessGame chessGame = chessGameFromDb(board, id);

        chessGame.move(Point.of(source), Point.of(destination));
        playLogDao.insert(new BoardDto(board), new GameStatusDto(chessGame), id);
        if (!chessGame.isOngoing() && chessGame.winner() != Team.NONE) {
            userDao.updateStatistics(id, chessGame.winner());
        }
        return new BoardDto(board);
    }

    private Board boardFromDb(String roomId) {
        return playLogDao.latestBoard(roomId).toEntity();
    }

    private ChessGame chessGameFromDb(Board board, String roomId) {
        GameStatusDto gameStatusDto = playLogDao.latestGameStatus(roomId);
        Turn turn = gameStatusDto.toTurnEntity();
        GameState gameState = gameStatusDto.toGameStateEntity(board);
        return new ChessGame(turn, new ScoreBoard(board), gameState);
    }
}
