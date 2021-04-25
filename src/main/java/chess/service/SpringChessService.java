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
import chess.exception.NotExistRoomException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpringChessService {
    private static final String SUCCEED_CODE = "SUCCEED";
    private static final String GAME_SET_CODE = "GAME_SET";
    private static final String FAIL_CODE = "FAIL";
    private static final String WHITE = "W";
    private static final String BLACK = "B";
    private static final String BLANK = ".";

    private SpringBoardDao springBoardDao;

    public SpringChessService(SpringBoardDao springBoardDao) {
        this.springBoardDao = springBoardDao;
    }

    public Map<String, String> currentBoardByRoomName(String roomName) {
        Board board = springBoardDao.findBoard(roomName).orElseThrow(NotExistRoomException::new);
        Map<String, String> boardName = new LinkedHashMap<>();
        for (Position position : board.getBoard().keySet()) {
            String positionName = position.positionName();
            boardName.put(positionName, pieceToName(board.getBoard().get(position)));
        }
        return boardName;
    }

    public ResponseDto move(PositionDto positionDTO, String roomName) {
        Board board = springBoardDao.findBoard(roomName).orElseThrow(NotExistRoomException::new);
        return moveExecute(positionDTO, board, roomName);
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

    private Side currentTurn(String roomName) throws ChessException {
        return springBoardDao.findTurn(roomName);
    }

    public void createRoom(String roomName) {
        springBoardDao.addBoard(roomName);
    }

    public void restartRoom(String roomName) {
        springBoardDao.updateBoard(Board.getGamingBoard(), "WHITE", roomName);
    }

    public RoomValidateDto checkDuplicatedRoom(String roomName) {
        if (springBoardDao.checkDuplicateByRoomName(roomName)) {
            return new RoomValidateDto(FAIL_CODE, "중복된 방 이름입니다.");
        }
        return new RoomValidateDto(SUCCEED_CODE, "방 생성 성공!");
    }

    public List<String> rooms() {
        return springBoardDao.findRooms();
    }

    public void deleteRoom(String roomName) {
        springBoardDao.deleteRoom(roomName);
    }

    public ScoreDto score(String roomName) {
        Board board = springBoardDao.findBoard(roomName)
                .orElseThrow(() -> new EmptyResultDataAccessException("방이 존재하지 않습니다.", 1));
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
