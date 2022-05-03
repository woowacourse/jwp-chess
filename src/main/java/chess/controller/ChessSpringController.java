package chess.controller;

import chess.dto.ResultDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessSpringController {
    private final ChessGameService chessGameService;

    public ChessSpringController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public ModelAndView home() {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roomsDto", chessGameService.getRooms());
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/boards/{id}")
    public ModelAndView board(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView
                .addObject("boardDto", chessGameService.getBoard(id));
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/boards/{id}/chess-status")
    public ModelAndView status(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView
                .addObject("status",
                        StatusDto.of(chessGameService.statusOfWhite(id), chessGameService.statusOfBlack(id)));
        modelAndView.setViewName("status");
        return modelAndView;
    }

    @GetMapping("/boards/{id}/chess-result")
    public ModelAndView result(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView
                .addObject("result", ResultDto
                        .of(chessGameService.statusOfWhite(id), chessGameService.statusOfBlack(id),
                                chessGameService.findWinner(id)));
        modelAndView.setViewName("result");
        return modelAndView;
    }
}
