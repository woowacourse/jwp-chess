package chess.web;

import chess.db.BoardDAO;
import chess.db.RoomDAO;
import chess.db.StateDAO;
import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.domain.score.ChessScore;
import chess.domain.state.State;
import chess.domain.state.StateSwitch;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebChessGame {

    private static final String DUPLICATED_ROOM_NAME = "이미 만들어진 방 이름입니다.";

    @Autowired
    private BoardDAO boardDAO;

    @Autowired
    private StateDAO stateDAO;

    @Autowired
    private RoomDAO roomDAO;

    private State state;

    public void initializeGame(BoardDTO boardDTO, ChessForm chessForm) {
        String roomName = chessForm.getRoomName();
        state = new State(Color.WHITE, StateSwitch.ON);
        boardDTO.generateUpdatedDTO(state.getBoard().getPieces());
        stateDAO.initializeRoom(roomName, chessForm.getPassword());
        String roomId = roomDAO.findIdByName(roomName);
        stateDAO.initializeColor(roomId);
    }

    public void validateDuplicateName(ChessForm chessForm) {
        if (roomDAO.isNameDuplicated(chessForm.getRoomName())) {
            throw new IllegalArgumentException(DUPLICATED_ROOM_NAME);
        }
    }

    public void executeOneTurn(ChessForm chessForm, MoveForm moveForm, BoardDTO boardDTO) {
        String roomName = chessForm.getRoomName();
        String roomId = roomDAO.findIdByName(roomName);
        Color nowColor = stateDAO.findColor(roomId);
        Color nextColor = nowColor.invert();
        state.movePiece(new Position(moveForm.getSource()), new Position(moveForm.getDest()), nowColor);
        state.changeColor();
        boardDTO.generateUpdatedDTO(state.getBoard().getPieces());
        stateDAO.convertColor(nextColor, roomId);
    }

    public String getColor(String roomName) {
        String roomId = roomDAO.findIdByName(roomName);
        if (stateDAO.findColor(roomId) == Color.WHITE) {
            return "백";
        }
        return "흑";
    }

    public ChessScore calculateScore() {
        return state.calculateScore();
    }

    public boolean isKingDead() {
        return state.isKingDead();
    }

    public void terminateState(String name) {
        String roomId = roomDAO.findIdByName(name);
        stateDAO.terminateState(roomId);
        state.changeSwitch();
    }

    public List<String> findAllSavedGame() {
        return roomDAO.findAllSavedName();
    }

    public List<String> findAllEndedGame() {
        return roomDAO.findAllEndedName();
    }

    public void getEndGameBoard(String name, BoardDTO boardDTO) {
        String roomId = roomDAO.findIdByName(name);
        boardDTO.generateUpdatedDTO(boardDAO.findAllPieces(roomId));
    }

    public String getMessageByPassWord(String name) {
        if (isEndedGame(name)) {
            return "방을 삭제하였습니다.";
        }
        return "방에 참여합니다.";
    }

    public boolean isEndedGame(String name) {
        return stateDAO.isEndedGame(roomDAO.findIdByName(name));
    }

    public boolean checkPassword(String roomName, String password) {
        return roomDAO.doesMatchWithPassword(password, roomName);
    }

    public void deleteRoom(String name) {
        roomDAO.deleteRoom(name);
    }

    public void saveBoard(String name) {
        String roomId = roomDAO.findIdByName(name);
        boardDAO.deleteBoard(roomId);
        boardDAO.insertBoard(state.getBoard(), roomId);
    }

    public void loadBoard(String name, BoardDTO boardDTO) {
        String roomId = roomDAO.findIdByName(name);
        Board nowBoard = new Board(boardDAO.findAllPieces(roomId));
        Color nowColor = stateDAO.findColor(roomId);
        this.state = new State(nowColor, StateSwitch.ON, nowBoard);
        boardDTO.generateUpdatedDTO(state.getBoard().getPieces());
    }
}
