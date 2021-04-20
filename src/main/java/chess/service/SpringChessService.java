package chess.service;

import chess.dao.SpringBoardDao;
import chess.domain.Side;
import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.PositionDto;
import chess.dto.ResponseDto;
import chess.dto.RoomValidateDto;
import chess.dto.ScoreDto;
import chess.exception.ChessException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpringChessService {
    private static final String SUCCEED_CODE = "200";
    private static final String GAME_SET_CODE = "300";
    private static final String FAIL_CODE = "400";
    private static final String WHITE = "W";
    private static final String BLACK = "B";
    private static final String BLANK = ".";

    private SpringBoardDao springBoardDao;

    public SpringChessService(SpringBoardDao springBoardDao) {
        this.springBoardDao = springBoardDao;
    }

    public Map<String, String> currentBoardByRoomName(String roomName) {
        Board board = springBoardDao.findBoard(roomName);
        Map<String, String> boardName = new LinkedHashMap<>();
        for (Position position : board.getBoard().keySet()) {
            String positionName = position.positionName();
            boardName.put(positionName, pieceToName(board.getBoard().get(position)));
        }
        return boardName;
    }

    public ResponseDto move(PositionDto positionDTO, String roomName) {
        Board board = springBoardDao.findBoard(roomName);
        try {
            return moveExecute(positionDTO, board, roomName);
        } catch (ChessException e) {
            return new ResponseDto(FAIL_CODE, e.getMessage(), currentTurn(roomName).name());
        }
    }

    private ResponseDto moveExecute(PositionDto positionDTO, Board board, String roomName) {
        board.move(Position.from(positionDTO.from()), Position.from(positionDTO.to()), currentTurn(roomName));
        springBoardDao.updateBoard(board, currentTurn(roomName).changeTurn().name(), roomName);
        if (board.isGameSet()) {
            Side side = board.winner();
            return new ResponseDto(GAME_SET_CODE, side.name(), "게임 종료(" + side.name() + " 승리)");
        }
        return new ResponseDto(SUCCEED_CODE, "Succeed", currentTurn(roomName).name());
    }

    public void restartBoard(String roomName) {
        springBoardDao.updateBoard(Board.getGamingBoard(), "WHITE", roomName);
    }

    public RoomValidateDto checkDuplicatedRoom(String roomName) {
        if (springBoardDao.checkDuplicateByRoomName(roomName)) {
            return new RoomValidateDto(FAIL_CODE, "중복된 방 이름입니다.");
        }
        return new RoomValidateDto(SUCCEED_CODE, "방 생성 성공!");
    }

    private Side currentTurn(String roomName) {
        return springBoardDao.findTurn(roomName);
    }

    public List<String> rooms() {
        return springBoardDao.findRooms();
    }

    public void createRoom(String roomName) {
        springBoardDao.newBoard(roomName);
    }

    public void deleteRoom(String roomName) {
        springBoardDao.deleteRoom(roomName);
    }

    public ScoreDto score(String roomName) {
        Board board = springBoardDao.findBoard(roomName);
        return new ScoreDto(String.valueOf(board.score(Side.WHITE)), String.valueOf(board.score(Side.BLACK)));
    }

    private String pieceToName(Piece piece) {
        if (piece.side() == Side.WHITE) {
            return WHITE + piece.getInitial().toUpperCase();
        }
        if (piece.side() == Side.BLACK) {
            return BLACK + piece.getInitial().toUpperCase();
        }
        return BLANK;
    }

    public String turnName(String roomName) {
        return currentTurn(roomName).name();
    }
}
