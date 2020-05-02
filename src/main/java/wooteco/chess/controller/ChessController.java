package wooteco.chess.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.dto.MoveDto;
import wooteco.chess.service.ChessService;

@Controller
public class ChessController {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/start")
    public String startGame(Model model) {
        return "chessGameStart";
    }

    @GetMapping("/playing/lastGame/{id}")
    public String lastGame(@PathVariable Long id) {
        chessService.playLastGame(id);
        return "redirect:/playing/" + chessService.getRoomNumber();
    }

    @GetMapping("/playing/newGame")
    public String newGame(@RequestParam String roomName) {
        chessService.playNewGame(roomName);
        return "redirect:/playing/" + chessService.getRoomNumber();
    }

    @GetMapping("/playing/{id}")
    public String Game(@PathVariable Long id, Model model) {
        model.addAttribute("roomNumber", chessService.getRoomNumber());
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
        chessService.move(moveDto.getRoomNumber(), moveDto.getSource(), moveDto.getTarget());
        return GSON.toJson(chessService.makeMoveResponse());
    }
}
