package chess.controller;

import chess.domain.exception.DataException;
import chess.service.ChessService;
import chess.view.ModelView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/rooms")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("")
    public ModelAndView play() throws DataException {
        final ModelAndView modelAndView = new ModelAndView("lobby");
        modelAndView.addAllObjects(ModelView.roomResponse(chessService.loadActiveRooms()));
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView play(@PathVariable String id) throws DataException {
        final ModelAndView modelAndView = new ModelAndView("chessGame");
        modelAndView.addAllObjects(ModelView.gameResponse(
                chessService.gameInfo(id),
                id
        ));
        return modelAndView;
    }

    @GetMapping("/end")
    public String endGame() {
        return "lobby";
    }
}
