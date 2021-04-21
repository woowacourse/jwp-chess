package chess.service;

import chess.dao.SpringChessLogDao;
import chess.dao.SpringChessRoomDao;
import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.dto.*;
import chess.exception.IllegalRoomException;
import chess.exception.InvalidMoveException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SpringChessService {
    private static final String END_TRUE = "true";

    private final SpringChessRoomDao springChessRoomDao;
    private final SpringChessLogDao springChessLogDao;

    public SpringChessService(SpringChessRoomDao springChessRoomDao, SpringChessLogDao springChessLogDao) {
        this.springChessRoomDao = springChessRoomDao;
        this.springChessLogDao = springChessLogDao;
    }

    public Long createRoom(RoomDto roomDto) {
        return springChessRoomDao.addRoom(roomDto);
    }

    public BoardDto loadRoom(String id) {
        return start(loadChessGame(id));
    }

    private ChessGame loadChessGame(String id) {
        validateRoom(id);
        List<CommandDto> commands = springChessLogDao.find(id);
        ChessGame chessGame = new ChessGame();

        chessGame.settingBoard();

        for (CommandDto command : commands) {
            chessGame.move(command.getTarget(), command.getDestination());
        }

        return chessGame;
    }

    private void validateRoom(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalRoomException("[ERROR] 방 이름은 공백이 될 수 없습니다.");
        }
    }

    public BoardDto move(MoveRequestDto moveRequestDto) {
        ChessGame chessGame = loadChessGame(moveRequestDto.getRoomId());

        BoardDto boardDto = movePiece(chessGame, moveRequestDto);
        return boardDto;
    }

    private BoardDto start(ChessGame chessGame) {
        Board board = chessGame.getBoard();
        return new BoardDto(board, chessGame.turn());
    }

    private BoardDto movePiece(ChessGame chessGame, MoveRequestDto moveRequestDto) {
        if (!chessGame.move(moveRequestDto.getTarget(), moveRequestDto.getDestination())) {
            throw new InvalidMoveException();
        }

        springChessLogDao.add(moveRequestDto);

        if (chessGame.isBeforeEnd()) {
            return new BoardDto(chessGame.getBoard(), chessGame.turn());
        }
        return new BoardDto(chessGame.getBoard(), chessGame.turn().name(), END_TRUE);
    }

    public List<String> movablePosition(MovablePositionDto movablePositionDto) {
        return loadChessGame(movablePositionDto.getRoomId()).findMovablePosition(movablePositionDto.getTarget());
    }

    public BoardStatusDto boardStatusDto(String roomId) {
        return new BoardStatusDto(loadChessGame(roomId).boardStatus());
    }

    public Optional<String> findRoomByName(String roomName) {
        return springChessLogDao.findRoomByName(roomName);
    }

    public Optional<String> findRoomById(String id) {
        return springChessLogDao.findRoomById(id);
    }

    public void deleteRoom(String roomNumber) {
        springChessLogDao.delete(roomNumber);
    }
}
