package chess.controller;

import chess.domain.event.MoveEvent;
import chess.domain.event.MoveRoute;
import chess.dto.CreateGameDto;
import chess.dto.GameDto;
import chess.service.ChessService;
import chess.util.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/game")
public class GameController {

    private static final String HTML_TEMPLATE_PATH = "game";

    private final ChessService chessService;

    public GameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/new")
    public CreateGameDto createGame() {
        return chessService.initGame();
    }

    @GetMapping("/{id}")
    public ModelAndView findGame(@PathVariable int id) {
        GameDto gameDto = chessService.findGame(id);
        return ResponseUtil.createModelAndView(HTML_TEMPLATE_PATH, gameDto);
    }

    @PostMapping("/{id}")
    public ModelAndView playGame(@PathVariable int id,
                                 @RequestBody MoveRoute moveRoute) {
        chessService.playGame(id, new MoveEvent(moveRoute));
        GameDto gameDto = chessService.findGame(id);
        return ResponseUtil.createModelAndView(HTML_TEMPLATE_PATH, gameDto);
    }
}
