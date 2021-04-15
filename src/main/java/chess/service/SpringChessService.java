package chess.service;

import chess.dao.SpringChessLogDao;
import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.dto.BoardDto;
import chess.dto.BoardStatusDto;
import chess.dto.MovablePositionDto;
import chess.dto.MoveRequestDto;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class SpringChessService {
    private static final String END_TRUE = "true";

    private final SpringChessLogDao springChessLogDao;

    public SpringChessService(SpringChessLogDao springChessLogDao) {
        this.springChessLogDao = springChessLogDao;
    }

    public BoardDto loadRoom(String roomNumber) {
        return start(loadChessGame(roomNumber));
    }

    private ChessGame loadChessGame(String roomNumber) {
        List<MoveRequestDto> commands = springChessLogDao.applyCommand(roomNumber);
        ChessGame chessGame = new ChessGame();
        chessGame.settingBoard();

        for (MoveRequestDto command : commands) {
            chessGame.move(command.getTarget(), command.getDestination());
        }

        return chessGame;
    }

    public BoardDto move(MoveRequestDto moveRequestDto) throws SQLException {
        ChessGame chessGame = loadChessGame(moveRequestDto.getRoomId());

        try {
            BoardDto boardDto = movePiece(chessGame, moveRequestDto);
            springChessLogDao.addLog(moveRequestDto);
            return boardDto;
        }
        catch (Exception e) {
            return start(chessGame);
        }
    }

    private BoardDto start(ChessGame chessGame) {
        Board board = chessGame.getBoard();
        return new BoardDto(board, chessGame.turn());
    }

    private BoardDto movePiece(ChessGame chessGame, MoveRequestDto moveRequestDto) {
        chessGame.move(moveRequestDto.getTarget(), moveRequestDto.getDestination());
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

    public void deleteRoom(String roomNumber) {
        springChessLogDao.deleteLog(roomNumber);
    }
}
