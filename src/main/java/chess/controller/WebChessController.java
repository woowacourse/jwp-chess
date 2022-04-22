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

    private final BoardDTO boardDTO = BoardDTO.buildModel();

    @Autowired
    private WebChessGame webChessGame;

    @GetMapping("/")
    public String inputGameID(Model model) {
        model.addAttribute("users", webChessGame.findAllUsers());
        return "index";
    }

    @PostMapping(value = "/preprocess")
    public String preprocess(RequestDTO command, Model model) {
        try {
            String roomId = command.getRoomId();
            model.addAttribute("roomId", roomId);
            webChessGame.searchSavedGame(roomId, boardDTO);
            if (webChessGame.isSaved(roomId)) {

                Set<String> keys = boardDTO.getData().keySet();
                for (String key : keys) {
                    model.addAttribute(key, boardDTO.getData().get(key));
                }

                Color savedColor = webChessGame.getColor(roomId);
                if (savedColor == Color.WHITE) {
                    return "white";
                }
                return "black";
            }
            return "new_game";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("exception", exception.getMessage());
            return "index_exception";
        }
    }

    @PostMapping("/ready")
    public String readyGame(RequestDTO command, Model model) {
        try {
            State now = State.create();
            State next = now.proceed(CommandConverter.convertCommand(RequestParser.from(command.getCommand()).getCommand()));
            webChessGame.initializeGame(boardDTO, next, command.getRoomId());
            model.addAttribute("roomId", command.getRoomId());
            if (webChessGame.isSaved(command.getRoomId())) {
                Set<String> keys = boardDTO.getData().keySet();
                for (String key : keys) {
                    model.addAttribute(key, boardDTO.getData().get(key));
                }
                return "white";
            }
            return "finished_before_start";
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException exception) {
            model.addAttribute("exception", exception.getMessage());
            return "ready_exception";
        }
    }

    @PostMapping("/move")
    public String runTurn(RequestDTO command, Model model) {
        State now = webChessGame.getState(command.getRoomId());
        model.addAttribute("roomId", command.getRoomId());
        try {
            Command parsedCommand = CommandConverter.convertCommand(RequestParser.from(command.getCommand()).getCommand());
            State next = webChessGame.executeOneTurn(parsedCommand, boardDTO, command.getRoomId());
            return executeOneTurn(parsedCommand, next, model);
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException exception) {
            return handleRunningException(command.getRoomId(), now, exception.getMessage(), model);
        }
    }

    private String executeOneTurn(Command command, State next, Model model) {
        if (next.isFinished()) {
            return "redirect:/finished";
        }
        Set<String> keys = boardDTO.getData().keySet();
        for (String key : keys) {
            model.addAttribute(key, boardDTO.getData().get(key));
        }
        if (command.isStatus()) {
            return "status";
        }
        if (!next.isWhite()) {
            return "black";
        }
        return "white";
    }

    private String handleRunningException(String roomId, State now, String message, Model model) {
        Set<String> keys = boardDTO.getData().keySet();
        model.addAttribute("roomId", roomId);
        for (String key : keys) {
            model.addAttribute(key, boardDTO.getData().get(key));
        }
        model.addAttribute("exception", message);
        if (now.isWhite()) {
            return "white_exception";
        }
        return "black_exception";
    }

    @PostMapping("/backwardToMove")
    public String backwardToMove(RequestDTO command, Model model) {
        State now = webChessGame.getState(command.getRoomId());
        Set<String> keys = boardDTO.getData().keySet();
        model.addAttribute("roomId", command.getRoomId());
        for (String key : keys) {
            model.addAttribute(key, boardDTO.getData().get(key));
        }
        if (now.isWhite()) {
            return "white";
        }
        return "black";
    }

    @PostMapping("/backwardToReady")
    public String backwardToReady(RequestDTO command, Model model) {
        Set<String> keys = boardDTO.getData().keySet();
        model.addAttribute("roomId", command.getRoomId());
        for (String key : keys) {
            model.addAttribute(key, boardDTO.getData().get(key));
        }
        return "new_game";
    }

    @PostMapping("/backwardToPreprocess")
    public String backwardToPreprocess() {
        return "index";
    }

    @PostMapping("/saved")
    public String saveGame(RequestDTO command, Model model) {
        model.addAttribute("roomId", command.getRoomId());
        Set<String> keys = boardDTO.getData().keySet();
        for (String key : keys) {
            model.addAttribute(key, boardDTO.getData().get(key));
        }
        return "saved";
    }

    @GetMapping("/finished")
    public String terminateGame(Model model) {
        Set<String> keys = boardDTO.getData().keySet();
        for (String key : keys) {
            model.addAttribute(key, boardDTO.getData().get(key));
        }
        return "finished";
    }
}
