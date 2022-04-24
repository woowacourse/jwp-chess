package chess.web.controller;

import chess.web.dto.MovePositionsDto;
import chess.web.service.ChessService;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String rendIndexPage(final Model model) {
        if (chessService.isNotRunning()) {
            return "redirect:/start";
        }
        model.addAttribute("chessStatus", chessService.getChessStatus());

        return "index";
    }

    @ResponseBody
    @PostMapping("/move")
    public String movePiece(@RequestBody MovePositionsDto movePositionsDto) {
        return new Gson().toJson(chessService.getMoveResult(movePositionsDto));
    }

    @GetMapping("/start")
    public String startChess() {
        chessService.start();

        return "redirect:/";
    }

    @GetMapping("/result")
    public String rendResultPage(final Model model) {
        model.addAttribute("result", chessService.getChessResult());
        chessService.end();

        return "result";
    }
}
