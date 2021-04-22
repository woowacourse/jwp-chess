package chess.controller.web;

import chess.controller.web.dto.game.GameResponseDto;
import chess.service.ChessService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SpringWebChessController {

    private final ChessService chessService;

    public SpringWebChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/games/{id}")
    public String findGameByGameId(@PathVariable Long id, Model model) {
        GameResponseDto gameResponseDto =
                new ModelMapper().map(chessService.findGameByGameId(id), GameResponseDto.class);
        model.addAttribute(gameResponseDto);
        return "board";
    }
}
