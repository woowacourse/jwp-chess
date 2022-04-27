package chess.controller;

import chess.domain.piece.Color;
import chess.domain.state.State;
import chess.web.BoardDTO;
import chess.web.ChessForm;
import chess.web.WebChessGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.NoSuchElementException;
import java.util.Set;

@Controller
public class WebChessController {

    private static final String WHITE_TURN_URL = "white";
    private static final String FINISHED_BEFORE_START_URL = "finished_before_start";
    private static final String READY_EXCEPTION_URL = "ready_exception";
    private static final String BLACK_TURN_URL = "black";
    private static final String NEW_GAME_URL = "new_game";
    private static final String INDEX_EXCEPTION_URL = "index_exception";
    private static final String INDEX_URL = "index";
    private static final String ROOM_ID = "roomId";
    private static final String REDIRECT_FINISHED_URL = "redirect:/finished";
    private static final String STATUS_URL = "status";
    private static final String WHITE_EXCEPTION_URL = "white_exception";
    private static final String BLACK_EXCEPTION_URL = "black_exception";
    private static final String SAVED_URL = "saved";
    private static final String FINISHED_URL = "finished";
    private static final String EXCEPTION = "exception";
    private final BoardDTO boardDTO = BoardDTO.buildModel();

    private final WebChessGame webChessGame;

    @Autowired
    public WebChessController(WebChessGame webChessGame) {
        this.webChessGame = webChessGame;
    }

    @GetMapping("/")
    public String inputGameID(Model model) {
        model.addAttribute("users", webChessGame.findAllUsers());
        return INDEX_URL;
    }

    @PostMapping(value = "/preprocess")
    public String preprocess(ChessForm command, Model model) {
        String roomId = command.getRoomId();
        model.addAttribute(ROOM_ID, roomId);
        try {
            webChessGame.searchSavedGame(roomId, boardDTO);
            return preprocessToReady(model, roomId);
        } catch (IllegalArgumentException exception) {
            model.addAttribute(EXCEPTION, exception.getMessage());
            return INDEX_EXCEPTION_URL;
        }
    }

    private String preprocessToReady(Model model, String roomId) {
        if (webChessGame.isSaved(roomId)) {
            updateDTO(model);
            return getColorUrl(webChessGame.getColor(roomId));
        }
        return NEW_GAME_URL;
    }

    private String getColorUrl(Color savedColor) {
        if (savedColor == Color.WHITE) {
            return WHITE_TURN_URL;
        }
        return BLACK_TURN_URL;
    }

    private void updateDTO(Model model) {
        Set<String> keys = boardDTO.getData().keySet();
        for (String key : keys) {
            model.addAttribute(key, boardDTO.getData().get(key));
        }
    }

    @PostMapping("/ready")
    public String readyGame(ChessForm chessForm, Model model) {
        model.addAttribute(ROOM_ID, chessForm.getRoomId());
        try {
            webChessGame.initializeGame(boardDTO, chessForm);
            return readyToRunning(chessForm, model);
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException exception) {
            model.addAttribute(EXCEPTION, exception.getMessage());
            return READY_EXCEPTION_URL;
        }
    }

    private String readyToRunning(ChessForm command, Model model) {
        if (webChessGame.isSaved(command.getRoomId())) {
            updateDTO(model);
            return WHITE_TURN_URL;
        }
        return FINISHED_BEFORE_START_URL;
    }

    @PostMapping("/move")
    public String runTurn(ChessForm chessForm, Model model) {
        State now = webChessGame.getState(chessForm.getRoomId());
        model.addAttribute(ROOM_ID, chessForm.getRoomId());
        try {
            State next = webChessGame.executeOneTurn(chessForm, boardDTO);
            return executeOneTurn(now, next, model);
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException exception) {
            return handleRunningException(chessForm.getRoomId(), now, exception.getMessage(), model);
        }
    }

    private String executeOneTurn(State now, State next, Model model) {
        if (next.isFinished()) {
            return REDIRECT_FINISHED_URL;
        }
        updateDTO(model);
        if (now.getColor() == next.getColor()) {
            return STATUS_URL;
        }
        return getColorUrl(next.getColor());
    }

    private String handleRunningException(String roomId, State now, String message, Model model) {
        model.addAttribute(ROOM_ID, roomId);
        updateDTO(model);
        model.addAttribute(EXCEPTION, message);
        if (now.isWhite()) {
            return WHITE_EXCEPTION_URL;
        }
        return BLACK_EXCEPTION_URL;
    }

    @PostMapping("/backwardToMove")
    public String backwardToMove(ChessForm command, Model model) {
        State now = webChessGame.getState(command.getRoomId());
        model.addAttribute(ROOM_ID, command.getRoomId());
        updateDTO(model);
        return getColorUrl(now.getColor());
    }

    @PostMapping("/backwardToReady")
    public String backwardToReady(ChessForm command, Model model) {
        model.addAttribute(ROOM_ID, command.getRoomId());
        updateDTO(model);
        return NEW_GAME_URL;
    }

    @PostMapping("/backwardToPreprocess")
    public String backwardToPreprocess() {
        return INDEX_URL;
    }

    @PostMapping("/saved")
    public String saveGame(ChessForm command, Model model) {
        model.addAttribute(ROOM_ID, command.getRoomId());
        updateDTO(model);
        return SAVED_URL;
    }

    @GetMapping("/finished")
    public String terminateGame(Model model) {
        updateDTO(model);
        return FINISHED_URL;
    }
}
