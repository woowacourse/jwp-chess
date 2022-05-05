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
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class WebChessGame {

    private static final String DUPLICATED_ROOM_NAME = "이미 만들어진 방 이름입니다.";

    private final BoardDAO boardDAO;
    private final StateDAO stateDAO;
    private final RoomDAO roomDAO;
    private State state;

    public WebChessGame(BoardDAO boardDAO, StateDAO stateDAO, RoomDAO roomDAO) {
        this.boardDAO = boardDAO;
        this.stateDAO = stateDAO;
        this.roomDAO = roomDAO;
    }

    private void updateModel(Model model, Board board) {
        BoardDTO boardDTO = BoardDTO.generateUpdatedDTO(board.getPieces());
        Set<String> keys = boardDTO.getData().keySet();
        for (String key : keys) {
            model.addAttribute(key, boardDTO.getData().get(key));
        }
    }

    public void findAllSavedGame(Model model) {
        model.addAttribute("names", roomDAO.findAllSavedName());
    }

    public void findAllEndedGame(Model model) {
        model.addAttribute("names", roomDAO.findAllEndedName());
    }

    public void generateNewGame(ChessForm chessForm, Model model) {
        String roomName = chessForm.getRoomName();
        String password = chessForm.getPassword();
        validateDuplicateName(roomName);
        initializeGame(roomName, password);
        saveBoard(roomName);
        updateModel(model, state.getBoard());
    }

    private void initializeGame(String roomName, String password) {
        state = new State(Color.WHITE, StateSwitch.ON);
        stateDAO.initializeRoom(roomName, password);
        String roomId = roomDAO.findIdByName(roomName);
        stateDAO.initializeColor(roomId);
    }

    private void validateDuplicateName(String roomName) {
        if (roomDAO.isNameExist(roomName)) {
            throw new IllegalArgumentException(DUPLICATED_ROOM_NAME);
        }
    }

    private void saveBoard(String name) {
        String roomId = roomDAO.findIdByName(name);
        boardDAO.deleteBoard(roomId);
        boardDAO.insertBoard(state.getBoard(), roomId);
    }

    public void runGameGetMethod(String roomName, Model model) {
        model.addAttribute("roomName", roomName);
        model.addAttribute("color", getColor(roomName));
        updateModel(model, state.getBoard());
    }

    private String getColor(String roomName) {
        String roomId = roomDAO.findIdByName(roomName);
        if (stateDAO.findColor(roomId) == Color.WHITE) {
            return "백";
        }
        return "흑";
    }

    public void runGamePostMethod(ChessForm chessForm, MoveForm moveForm, Model model) {
        try {
            executeOneTurn(chessForm, moveForm);
            updateModel(model, state.getBoard());
            model.addAttribute("color", getColor(chessForm.getRoomName()));
        }
        catch (IllegalArgumentException exception) {
            updateModel(model, state.getBoard());
            throw new IllegalArgumentException(exception.getMessage());
        }
    }

    private void executeOneTurn(ChessForm chessForm, MoveForm moveForm) {
        String roomName = chessForm.getRoomName();
        String roomId = roomDAO.findIdByName(roomName);
        Color nowColor = stateDAO.findColor(roomId);
        Color nextColor = nowColor.invert();
        state.movePiece(new Position(moveForm.getSource()), new Position(moveForm.getDest()), nowColor);
        state.changeColor();
        stateDAO.convertColor(nextColor, roomId);
    }

    public void gameScore(ChessForm chessForm, Model model) {
        ChessScore chessScore = calculateScore();
        model.addAttribute("roomName", chessForm.getRoomName());
        model.addAttribute("whiteScore", chessScore.getWhiteScore());
        model.addAttribute("blackScore", chessScore.getBlackScore());
        updateModel(model, state.getBoard());
    }

    private ChessScore calculateScore() {
        return state.calculateScore();
    }

    public void gameSave(ChessForm chessForm, Model model) {
        String roomName = chessForm.getRoomName();
        saveBoard(roomName);
        model.addAttribute("roomName", roomName);
        updateModel(model, state.getBoard());
    }

    public void gameEnd(String roomName, Model model) {
        model.addAttribute("roomName", roomName);
        String roomId = roomDAO.findIdByName(roomName);
        terminateState(roomName);
        boardDAO.deleteBoard(roomId);
        boardDAO.insertBoard(state.getBoard(), roomId);
        updateModel(model, state.getBoard());
    }

    private void terminateState(String name) {
        String roomId = roomDAO.findIdByName(name);
        stateDAO.terminateState(roomId);
        state.changeSwitch();
    }

    public boolean isKingDead() {
        return state.isKingDead();
    }

    public void checkPassword(String roomName, Model model) {
        model.addAttribute("roomName", roomName);
        if (isEndedGame(roomName)) {
            String roomId = roomDAO.findIdByName(roomName);
            Board endedGameBoard = new Board(boardDAO.findAllPieces(roomId));
            Color endedGameColor = stateDAO.findColor(roomId);
            state = new State(endedGameColor, StateSwitch.OFF, endedGameBoard);
            updateModel(model, state.getBoard());
            model.addAttribute("passwordtype", "을 삭제");
            return;
        }
        model.addAttribute("passwordtype", "에 참여");
    }

    public String getMessageByPassWord(String name) {
        if (isEndedGame(name)) {
            return "방을 삭제하였습니다.";
        }
        return "방에 참여합니다.";
    }

    public boolean doesNameExist(String name) {
        return roomDAO.isNameExist(name);
    }

    public boolean isEndedGame(String name) {
        return stateDAO.isEndedGame(roomDAO.findIdByName(name));
    }

    public boolean isPasswordSame(String roomName, String password) {
        return roomDAO.doesMatchWithPassword(password, roomName);
    }

    public void redirectByCondition(String roomName, Model model) {
        if (isEndedGame(roomName)) {
            deleteRoom(roomName);
            return;
        }
        loadBoard(roomName, model);
    }

    private void deleteRoom(String name) {
        roomDAO.deleteRoom(name);
    }

    private void loadBoard(String name, Model model) {
        String roomId = roomDAO.findIdByName(name);
        Board nowBoard = new Board(boardDAO.findAllPieces(roomId));
        Color nowColor = stateDAO.findColor(roomId);
        this.state = new State(nowColor, StateSwitch.ON, nowBoard);
        updateModel(model, state.getBoard());
    }
}
