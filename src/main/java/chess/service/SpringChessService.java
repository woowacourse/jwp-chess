package chess.service;

import chess.dao.SpringChessLogDao;
import chess.dao.SpringChessRoomDao;
import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.dto.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class SpringChessService {
    private static final String END_TRUE = "true";

    private final SpringChessLogDao springChessLogDao;
    private final SpringChessRoomDao chessRoomDao;

    public SpringChessService(SpringChessLogDao springChessLogDao, SpringChessRoomDao chessRoomDao) {
        this.springChessLogDao = springChessLogDao;
        this.chessRoomDao = chessRoomDao;
    }

    public BoardDto loadRoom(String roomNumber) {
        return start(loadChessGame(roomNumber));
    }

    private ChessGame loadChessGame(String roomNumber) {
        List<String> commands = springChessLogDao.applyCommand(roomNumber);
        ChessGame chessGame = new ChessGame();
        chessGame.settingBoard();

        for (String command : commands) {
            String[] movePositions = command.split(",");
            String target = movePositions[0];
            String destination = movePositions[1];
            chessGame.move(target, destination);
        }

        return chessGame;
    }

    public BoardDto move(MoveRequestDto moveRequestDto) throws SQLException {
        ChessGame chessGame = loadChessGame(moveRequestDto.getRoomId());

        BoardDto boardDto = movePiece(chessGame, moveRequestDto);
        return boardDto;
    }

    private BoardDto start(ChessGame chessGame) {
        Board board = chessGame.getBoard();
        return new BoardDto(board, chessGame.turn());
    }

    private BoardDto movePiece(ChessGame chessGame, MoveRequestDto moveRequestDto) {
        chessGame.move(moveRequestDto.getTarget(), moveRequestDto.getDestination());
        springChessLogDao.addLog(moveRequestDto.getRoomId(), moveRequestDto.getTarget(), moveRequestDto.getDestination());

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

    public void resetRoom(String roomNumber) {
        springChessLogDao.deleteLog(roomNumber);
    }

    public String createRoom(String name) {
        return chessRoomDao.createRoom(name);
    }

    public List<RoomDto> roomIds() {
        return chessRoomDao.findAllRoomIds();
    }

    public void deleteRoom(String roomNumber) {
        chessRoomDao.deleteRoom(roomNumber);
        springChessLogDao.deleteLog(roomNumber);
    }
}
