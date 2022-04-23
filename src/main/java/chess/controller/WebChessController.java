package chess.controller;

import chess.dto.MoveRequestDto;
import chess.dto.ScoreDto;
import chess.service.ChessGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebChessController {

    private static final String CHESS_GAME_URL = "chessGame";
    private static final String START_URL = "start";

    @Autowired
    private ChessGameService service;

    @GetMapping("/")
    public String main() {
        return START_URL;
    }

    @GetMapping("/game")
    public String game(Model model) {
        return executeCommand(service::init, model);
    }

    @GetMapping("/start")
    public String start(Model model) {
        return executeCommand(service::start, model);
    }

    @GetMapping("/end")
    public String end(Model model) {
        return executeCommand(service::end, model);
    }

    @GetMapping("/restart")
    public String restart(Model model) {
        return executeCommand(service::restart, model);
    }

    @GetMapping("/save")
    public String save(Model model) {
        return executeCommand(service::save, model);
    }

    @GetMapping("status")
    public String status(Model model) {
        return executeCommand(() -> {
            ScoreDto score = service.status();
            model.addAttribute("score", score);
        }, model);
    }

    @PostMapping("/move")
    public String move(MoveRequestDto moveRequest, Model model) {
        return executeCommand(() -> service.move(moveRequest.getFrom(), moveRequest.getTo()), model);
    }

    private String executeCommand(Runnable command, Model model) {
        try {
            command.run();
            model.addAttribute("pieces", service.getPiecesByUnicode());
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return CHESS_GAME_URL;
    }

}
