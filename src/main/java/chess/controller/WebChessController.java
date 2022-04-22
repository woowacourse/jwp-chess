package chess.controller;

import static spark.Spark.get;
import static spark.Spark.post;

import chess.db.StateDAO;
import chess.domain.command.Command;
import chess.domain.command.CommandConverter;
import chess.domain.piece.Color;
import chess.domain.state.State;
import chess.web.BoardDTO;
import chess.web.RequestParser;
import chess.web.WebChessGame;

import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebChessController {

    private final BoardDTO boardDTO = BoardDTO.buildModel();
    private final WebChessGame webChessGame = new WebChessGame();

    @GetMapping("/")
    public String inputGameID(Model model) {
        model.addAttribute("users", new StateDAO().findAllUsers());
        return "index";
    }

    @PostMapping("/preprocess")
    public String preprocess(@RequestBody String command, Model model) {
        try {
            String roomId = RequestParser.from(command).getRoomID();
            webChessGame.searchSavedGame(roomId, boardDTO);
            if (webChessGame.isSaved()) {

                Set<String> keys = boardDTO.getData().keySet();
                for (String key : keys) {
                    model.addAttribute(key, boardDTO.getData().get(key));
                }

                Color savedColor = webChessGame.getColor();
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
    public String readyGame(@RequestBody String command, Model model) {
        try {
            State now = State.create();
            State next = now.proceed(CommandConverter.convertCommand(RequestParser.from(command).getCommand()));
            webChessGame.initializeGame(boardDTO, next);
            if (webChessGame.isSaved()) {
                Set<String> keys = boardDTO.getData().keySet();
                for (String key : keys) {
                    model.addAttribute(key, boardDTO.getData().get(key));
                }
                return "white";
            }
            return"finished_before_start";
        }
        catch (IllegalArgumentException | IllegalStateException | NoSuchElementException exception) {
            model.addAttribute("exception", exception.getMessage());
            return "ready_exception";
        }
    }

    @PostMapping("/move")
    public String runTurn(@RequestBody String command, Model model) {
        State now = webChessGame.getState();
        try {
            Command parsedCommand = CommandConverter.convertCommand(RequestParser.from(command).getCommand());
            State next = webChessGame.executeOneTurn(parsedCommand, boardDTO);
            return executeOneTurn(parsedCommand, next, model);
        }
        catch (IllegalArgumentException | IllegalStateException | NoSuchElementException exception) {
            return handleRunningException(now, exception.getMessage(), model);
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

    private String handleRunningException(State now, String message, Model model) {
        Set<String> keys = boardDTO.getData().keySet();
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
    public String backwardToMove(Model model) {
        State now = webChessGame.getState();
        Set<String> keys = boardDTO.getData().keySet();
        for (String key : keys) {
            model.addAttribute(key, boardDTO.getData().get(key));
        }
        if (now.isWhite()) {
            return "white";
        }
        return "black";
    }

    @PostMapping("/backwardToReady")
    public String backwardToReady(Model model) {
        Set<String> keys = boardDTO.getData().keySet();
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
    public String saveGame(Model model) {
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
