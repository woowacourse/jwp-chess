package chess.web.controller;

import chess.web.dto.MovePositionsDto;
import chess.web.dto.MoveResultDto;
import chess.web.service.ChessService;
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
    public String showIndex(final Model model) {
        model.addAttribute("chessStatus", chessService.getChessStatus());

        return "index";
    }

//    @ResponseBody
    @PostMapping("/move")
    public MoveResultDto movePiece(@RequestBody MovePositionsDto movePositionsDto) {
        return chessService.getMoveResult(movePositionsDto);
    }

    @GetMapping("/result")
    public String showResult(final Model model) {
        model.addAttribute("result", chessService.getChessResult());
        chessService.restart();

        return "result";
    }
}
