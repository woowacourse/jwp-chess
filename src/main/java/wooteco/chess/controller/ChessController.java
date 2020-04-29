package wooteco.chess.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import wooteco.chess.dto.MoveDto;
import wooteco.chess.service.ChessService;

@Controller
public class ChessController {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String startGame(Model model) {
        chessService.start();
        model.addAllAttributes(chessService.makeStartResponse());
        return "chessGameStart";
    }

    @GetMapping("/playing/lastGame")
    public String lastGame(Model model) {
        chessService.playLastGame();
        model.addAllAttributes(chessService.makeMoveResponse());
        return "chessGame";
    }

    @GetMapping("/playing/newGame")
    public String newGame(Model model) {
        chessService.playNewGame();
        model.addAllAttributes(chessService.makeMoveResponse());
        return "chessGame";
    }

    @GetMapping("/end")
    public String endGame() {
        chessService.end();
        return "chessGameEnd";
    }

    @PostMapping("/playing/move")
    @ResponseBody
    public String move(@RequestBody MoveDto moveDto) {
        chessService.move(moveDto.getSource(), moveDto.getTarget());
        return GSON.toJson(chessService.makeMoveResponse());
    }
}
