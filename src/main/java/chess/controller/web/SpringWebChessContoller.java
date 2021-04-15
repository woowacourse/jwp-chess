package chess.controller.web;

import chess.controller.web.dto.game.GameResponseDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SpringWebChessContoller {

    private final ChessService chessService;

    public SpringWebChessContoller(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/game/{id}")
    public String findGameByGameId(@PathVariable Long id, Model model) {
        GameResponseDto gameResponseDto = chessService.findGameByGameId(id);
        model.addAttribute(gameResponseDto);
        return "board";
    }
}
