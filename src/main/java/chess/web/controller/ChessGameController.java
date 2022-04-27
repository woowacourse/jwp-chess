package chess.web.controller;

import chess.domain.piece.Piece;
import chess.web.dto.PlayResultDto;
import chess.web.dto.ScoreDto;
import chess.web.service.ChessGameService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessGameController {

    private final ChessGameService service;

    public ChessGameController(ChessGameService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/start")
    public String start() {
        service.start();
        return "redirect:/play";
    }

    @GetMapping("/play")
    public ModelAndView play() {
        ModelAndView modelAndView = new ModelAndView("playing");
        PlayResultDto playResultDto = service.play();
        Map<String, Piece> board = playResultDto.getBoard();
        for (String position : board.keySet()) {
            modelAndView.addObject(position, board.get(position));
        }
        modelAndView.addObject("turn", playResultDto.getTurn());

        return modelAndView;
    }

    @GetMapping("/end")
    public ModelAndView end() {
        ModelAndView modelAndView = new ModelAndView("finished");
        ScoreDto scores = service.status();
        modelAndView.addObject("whiteScore", scores.getWhiteScore());
        modelAndView.addObject("blackScore", scores.getBlackScore());

        return modelAndView;
    }
}
