package chess.controller;

import chess.dto.GameRequestDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/chess")
@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/creation")
    public String initializeChess(@RequestBody final GameRequestDto gameRequestDto) {
        final long id = chessService.initializeChess(gameRequestDto);
        return "redirect:/games/" + id;
    }

}
