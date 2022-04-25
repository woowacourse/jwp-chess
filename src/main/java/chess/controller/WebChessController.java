package chess.controller;

import chess.domain.command.Command;
import chess.domain.command.CommandConverter;
import chess.domain.piece.Color;
import chess.domain.state.State;
import chess.web.BoardDTO;
import chess.web.RequestDTO;
import chess.web.RequestParser;
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

    @Autowired
    private final WebChessGame webChessGame = new WebChessGame();

    @GetMapping("/")
    public String inputGameID(Model model) {
        model.addAttribute("users", webChessGame.findAllUsers());
        return INDEX_URL;
    }

    @PostMapping(value = "/preprocess")
    public String preprocess(RequestDTO command, Model model) {
        try {
            String roomId = command.getRoomId();
            model.addAttribute(ROOM_ID, roomId);
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
    public String readyGame(RequestDTO command, Model model) {
        try {
            State next = State.create()
                    .proceed(CommandConverter.convertCommand(RequestParser.from(command.getCommand()).getCommand()));
            webChessGame.initializeGame(boardDTO, next, command.getRoomId());
            model.addAttribute(ROOM_ID, command.getRoomId());
            return readyToRunning(command, model);
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException exception) {
            model.addAttribute(EXCEPTION, exception.getMessage());
            return READY_EXCEPTION_URL;
        }
    }

    private String readyToRunning(RequestDTO command, Model model) {
        if (webChessGame.isSaved(command.getRoomId())) {
            updateDTO(model);
            return WHITE_TURN_URL;
        }
        return FINISHED_BEFORE_START_URL;
    }

    @PostMapping("/move")
    public String runTurn(RequestDTO command, Model model) {
        State now = webChessGame.getState(command.getRoomId());
        model.addAttribute(ROOM_ID, command.getRoomId());
        try {
            Command parsedCommand = CommandConverter.convertCommand(
                    RequestParser.from(command.getCommand()).getCommand());
            State next = webChessGame.executeOneTurn(parsedCommand, boardDTO, command.getRoomId());
            return executeOneTurn(parsedCommand, next, model);
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException exception) {
            return handleRunningException(command.getRoomId(), now, exception.getMessage(), model);
        }
    }

    private String executeOneTurn(Command command, State next, Model model) {
        if (next.isFinished()) {
            return REDIRECT_FINISHED_URL;
        }
        updateDTO(model);
        if (command.isStatus()) {
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
    public String backwardToMove(RequestDTO command, Model model) {
        State now = webChessGame.getState(command.getRoomId());
        model.addAttribute(ROOM_ID, command.getRoomId());
        updateDTO(model);
        return getColorUrl(now.getColor());
    }

    @PostMapping("/backwardToReady")
    public String backwardToReady(RequestDTO command, Model model) {
        model.addAttribute(ROOM_ID, command.getRoomId());
        updateDTO(model);
        return NEW_GAME_URL;
    }

    @PostMapping("/backwardToPreprocess")
    public String backwardToPreprocess() {
        return INDEX_URL;
    }

    @PostMapping("/saved")
    public String saveGame(RequestDTO command, Model model) {
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
