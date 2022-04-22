package chess.controller;

import chess.dto.MoveDto;
import chess.service.ChessService;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class WebController {

    private final Gson gson = new Gson();
    private final ChessService chessService = new ChessService();

    @GetMapping("/")
    public String showFirstPage() {
        return "index";
    }

    @GetMapping("/start")
    @ResponseBody
    public String startGame() {
        return gson.toJson(chessService.newGame());
    }

    @GetMapping("/restart")
    @ResponseBody
    public String restart() {
        return gson.toJson(chessService.loadGame());
    }

    @PutMapping("/move")
    @ResponseBody
    public String move(@RequestBody MoveDto moveDto) {
        return gson.toJson(chessService.move(moveDto.getFrom(), moveDto.getTo()));
    }

    @ExceptionHandler({RuntimeException.class})
    private ResponseEntity<String> handleException(final RuntimeException exception) {
        ResponseEntity.status(500);
        return ResponseEntity.badRequest().body(gson.toJson(exception.getMessage()));
    }
}
