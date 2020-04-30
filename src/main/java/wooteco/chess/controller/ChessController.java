package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import wooteco.chess.service.ChessService;
import wooteco.chess.utils.ModelParser;

import javax.servlet.ServletRequest;

@Controller
public class ChessController {
    private static final String INDEX_PAGE = "/index";

    @Autowired
    ChessService chessService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAllAttributes(chessService.loadInitBoard());
        return INDEX_PAGE;
    }

    @PostMapping("/new-game")
    public String newGame(Model model) {
        model.addAllAttributes(ModelParser.parseBoard(chessService.newGame()));
        return INDEX_PAGE;
    }

    @PostMapping("/load-game")
    public String loadGame(Model model) {
        model.addAllAttributes(ModelParser.parseBoard(chessService.readBoard()));
        return INDEX_PAGE;
    }

    @PostMapping("/score")
    public String score(Model model) {
        model.addAllAttributes(chessService.readBoardWithScore());
        return INDEX_PAGE;
    }

    @PostMapping("/move")
    public String move(ServletRequest request, Model model) {
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        chessService.move(start, end);

        model.addAllAttributes(ModelParser.parseBoard(chessService.readBoard()));
        return INDEX_PAGE;
    }

    @PostMapping("/show-movable")
    public String showMovable(ServletRequest request, Model model) {
        String startPosition = request.getParameter("start");

        model.addAllAttributes(chessService.loadMovable(startPosition));
        return INDEX_PAGE;
    }
}