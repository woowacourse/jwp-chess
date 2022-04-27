package chess.web;

import chess.db.BoardDAO;
import chess.db.StateDAO;
import chess.domain.command.Command;
import chess.domain.command.CommandConverter;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WebChessGame {

    @Autowired
    private BoardDAO boardDAO;

    @Autowired
    private StateDAO stateDAO;

    public State getState(String roomId) {
        Color color = stateDAO.findColor(roomId);
        Map<Position, Piece> pieces = boardDAO.findAllPieces(roomId);
        return State.create(pieces, color);
    }

    public void searchSavedGame(String roomId, BoardDTO boardDTO) {
        if (stateDAO.isSaved(roomId)) {
            State now = getState(roomId);
            boardDTO.generateUpdatedDTO(now.getBoard());
        }
    }

    public boolean isSaved(String roomId) {
        return stateDAO.isSaved(roomId);
    }

    public Color getColor(String roomId) {
        return getState(roomId).getColor();
    }

    public void initializeGame(BoardDTO boardDTO, ChessForm chessForm) {
        State next = State.create()
                .proceed(CommandConverter.convertCommand(RequestParser.from(chessForm.getCommand()).getCommand()));
        boardDTO.generateUpdatedDTO(next.getBoard());
        String roomId = chessForm.getRoomId();
        if (next.isRunning()) {
            stateDAO.initializeID(roomId);
            stateDAO.initializeColor(roomId);
            boardDAO.initializePieces(next, roomId);
        }
    }

    public String findAllUsers() {
        return stateDAO.findAllUsers();
    }

    public State executeOneTurn(ChessForm chessForm, BoardDTO boardDTO) {
        Command parsedCommand = CommandConverter.convertCommand(
                RequestParser.from(chessForm.getCommand()).getCommand());
        String roomId = chessForm.getRoomId();
        State next = getState(roomId).proceed(parsedCommand);
        executeWhenFinished(boardDTO, next, roomId);
        executeWhenShowScore(parsedCommand, boardDTO, next);
        executeWhenMoveEnd(parsedCommand, boardDTO, next, roomId);
        return next;
    }

    private void executeWhenMoveEnd(Command command, BoardDTO boardDTO, State next, String roomId) {
        if (command.isMove()) {
            endTurn(next, command, boardDTO, roomId);
        }
    }

    private void executeWhenShowScore(Command command, BoardDTO boardDTO, State next) {
        if (command.isStatus()) {
            boardDTO.updateWithScore(next.getBoard(), next.generateScore());
        }
    }

    private void executeWhenFinished(BoardDTO boardDTO, State next, String roomId) {
        if (next.isFinished()) {
            boardDTO.generateUpdatedDTO(next.getBoard());
            stateDAO.terminateDB(roomId);
        }
    }

    private void endTurn(State next, Command command, BoardDTO boardDTO, String roomId) {
        boardDTO.generateUpdatedDTO(next.getBoard());
        movePieceIntoDB(next, command, roomId);
        if (!next.isWhite()) {
            stateDAO.convertColor(Color.BLACK, roomId);
            return;
        }
        stateDAO.convertColor(Color.WHITE, roomId);
    }

    private void movePieceIntoDB(State next, Command command, String roomId) {
        Position from = command.getFromPosition();
        Position to = command.getToPosition();
        boardDAO.delete(from, roomId);
        boardDAO.delete(to, roomId);
        boardDAO.insert(to, next.getBoard().findPiece(to), roomId);
    }
}
