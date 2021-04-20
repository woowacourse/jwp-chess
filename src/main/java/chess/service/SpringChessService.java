package chess.service;

import chess.domain.board.Board;
import chess.domain.board.Point;
import chess.domain.board.Team;
import chess.domain.chessgame.ChessGame;
import chess.domain.chessgame.ScoreBoard;
import chess.domain.chessgame.Turn;
import chess.domain.gamestate.GameState;
import chess.dto.web.*;
import chess.repository.PlayLogRepository;
import chess.repository.RoomRepository;
import chess.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpringChessService {

    private RoomRepository roomRepository;
    private UserRepository userRepository;
    private PlayLogRepository playLogRepository;

    public SpringChessService(RoomRepository roomRepository, UserRepository userRepository, PlayLogRepository playLogRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.playLogRepository = playLogRepository;
    }

    public List<RoomDto> openedRooms() {
        return roomRepository.openedRooms();
    }

    public BoardDto latestBoard(String id) {
        return playLogRepository.latestBoard(id);
    }

    public UsersInRoomDto usersInRoom(String id) {
        return userRepository.usersInRoom(id);
    }

    public RoomIdDto create(RoomDto roomDto) {
        if (userRepository.isNotExist(roomDto.getWhite())) {
            userRepository.insert(roomDto.getWhite());
        }

        if (userRepository.isNotExist(roomDto.getBlack())) {
            userRepository.insert(roomDto.getBlack());
        }
        return new RoomIdDto(roomRepository.insert(roomDto));
    }

    public GameStatusDto gameStatus(String id) {
        Board board = boardFromDb(id);
        ChessGame chessGame = chessGameFromDb(board, id);
        return new GameStatusDto(chessGame);
    }

    private Board boardFromDb(String roomId) {
        return playLogRepository.latestBoard(roomId).toEntity();
    }

    private ChessGame chessGameFromDb(Board board, String roomId) {
        GameStatusDto gameStatusDto = playLogRepository.latestGameStatus(roomId);
        Turn turn = gameStatusDto.toTurnEntity();
        GameState gameState = gameStatusDto.toGameStateEntity(board);
        return new ChessGame(turn, new ScoreBoard(board), gameState);
    }

    public BoardDto start(String id) {
        Board board = playLogRepository.latestBoard(id).toEntity();
        ChessGame chessGame = chessGameFromDb(board, id);
        chessGame.start();
        BoardDto boardDto = new BoardDto(board);
        GameStatusDto gameStatusDto = new GameStatusDto(chessGame);
        playLogRepository.insert(boardDto, gameStatusDto, id);
        return new BoardDto(board);
    }

    public void exit(String id) {
        Board board = boardFromDb(id);
        ChessGame chessGame = chessGameFromDb(board, id);
        chessGame.end();
        BoardDto boardDto = new BoardDto(board);
        GameStatusDto gameStatusDto = new GameStatusDto(chessGame);
        playLogRepository.insert(boardDto, gameStatusDto, id);
    }

    public void close(String id) {
        roomRepository.close(id);
    }

    public List<PointDto> movablePoints(String id, String point) {
        Board board = boardFromDb(id);
        ChessGame chessGame = chessGameFromDb(board, id);
        List<Point> movablePoints = chessGame.movablePoints(Point.of(point));
        return movablePoints.stream()
                .map(PointDto::new)
                .collect(Collectors.toList());
    }

    public BoardDto move(String id, String source, String destination) {
        Board board = boardFromDb(id);
        ChessGame chessGame = chessGameFromDb(board, id);

        chessGame.move(Point.of(source), Point.of(destination));
        playLogRepository.insert(new BoardDto(board), new GameStatusDto(chessGame), id);
        if (!chessGame.isOngoing() && chessGame.winner() != Team.NONE) {
            userRepository.updateStatistics(id, chessGame.winner());
        }
        return new BoardDto(board);
    }
}
