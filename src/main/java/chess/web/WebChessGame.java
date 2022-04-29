package chess.web;

import chess.db.BoardDAO;
import chess.db.RoomDAO;
import chess.db.StateDAO;
import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.domain.score.ChessScore;
import chess.domain.score.ScoreCalculator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebChessGame {

    private static final String DUPLICATED_ROOM_NAME = "이미 만들어진 방 이름입니다.";
    private static final String CANNOT_MOVE_OPPONENT_PIECE = "상대 말을 움직이게 할 수 없습니다.";

    @Autowired
    private BoardDAO boardDAO;

    @Autowired
    private StateDAO stateDAO;

    @Autowired
    private RoomDAO roomDAO;

    private final Board board = new Board();

    public void initializeGame(BoardDTO boardDTO, ChessForm chessForm) {
        String roomName = chessForm.getRoomName();
        boardDTO.generateUpdatedDTO(board);
        stateDAO.initializeRoom(roomName, chessForm.getPassword());
        String roomId = roomDAO.findIdByName(roomName);
        stateDAO.initializeColor(roomId);
    }

    public void validateDuplicateName(ChessForm chessForm) {
        if (roomDAO.isNameDuplicated(chessForm.getRoomName())) {
            throw new IllegalArgumentException(DUPLICATED_ROOM_NAME);
        }
    }

    private void validateMoveOpponentPiece(ChessForm chessForm, MoveForm moveForm) {
        String roomName = chessForm.getRoomName();
        String roomId = roomDAO.findIdByName(roomName);
        Color nowColor = stateDAO.findColor(roomId);
        if (board.findPiece(new Position(moveForm.getSource())).isSameColor(nowColor.invert())) {
            throw new IllegalArgumentException(CANNOT_MOVE_OPPONENT_PIECE);
        }
    }

    public void executeOneTurn(ChessForm chessForm, MoveForm moveForm, BoardDTO boardDTO) {
        String roomName = chessForm.getRoomName();
        String roomId = roomDAO.findIdByName(roomName);
        Color nowColor = stateDAO.findColor(roomId);
        Color nextColor = nowColor.invert();
        validateMoveOpponentPiece(chessForm, moveForm);
        board.movePiece(new Position(moveForm.getSource()), new Position(moveForm.getDest()));
        boardDTO.generateUpdatedDTO(board);
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
        return ScoreCalculator.calculateChessScore(board.getPieces());
    }

    public boolean isKingDead(ChessForm chessForm) {
        String roomId = roomDAO.findIdByName(chessForm.getRoomName());
        return board.isKingNotExist(stateDAO.findColor(roomId));
    }

    public void terminateState(String name) {
        String roomId = roomDAO.findIdByName(name);
        stateDAO.terminateState(roomId);
    }

    public List<String> findAllSavedGame() {
        return roomDAO.findAllSavedName();
    }

    public List<String> findAllEndedGame() {
        return roomDAO.findAllEndedName();
    }
}
