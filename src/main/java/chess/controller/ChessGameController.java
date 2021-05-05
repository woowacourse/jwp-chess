package chess.controller;

import chess.service.SpringChessService;
import chess.service.dto.ChessInfosDto;
import chess.service.dto.TilesDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessGameController {

    private final SpringChessService chessService;

    public ChessGameController(final SpringChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String main(Model model) {
        ChessInfosDto chessInfosDto = chessService.findAllGame();
        model.addAttribute("chessInfosDto", chessInfosDto);
        return "main";
    }

    @GetMapping("/games")
    public String startPage(Model model) {
        TilesDto tilesDto = chessService.emptyBoard();
        model.addAttribute("tilesDto", tilesDto);
        return "board";
    }

    @GetMapping("/signin")
    public String signin() {
        return "signInForm";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signUpForm";
    }
}
