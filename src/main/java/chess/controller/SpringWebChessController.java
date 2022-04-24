package chess.controller;

import chess.dto.BoardDto;
import chess.service.spring.ChessService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringWebChessController {

    private final ChessService chessService;

    @Autowired
    public SpringWebChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public ModelAndView selectGame() {
        BoardDto boardDto = chessService.selectBoard();

        Map<String, Object> model = new HashMap<>();
        model.put("board", boardDto);

        return new ModelAndView("index", model);
    }

    @GetMapping("/game")
    public ModelAndView insertGame() {
        chessService.insertGame();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/");
        return mv;
    }
}
