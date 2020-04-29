package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wooteco.chess.domain.repository.ChessRepository;

@Controller
public class WebChessGameController {
    ChessRepository chessRepository;

    public WebChessGameController(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    @GetMapping("/game/{id}")
    public String renderGamePage(Model model, @PathVariable String id) {
        if (chessRepository.selectAll().contains(Integer.parseInt(id))) {
            model.addAttribute("id", id);
            return "game";
        }
        return "index";
    }
}
