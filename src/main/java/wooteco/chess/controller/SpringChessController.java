package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.domain.game.NormalStatus;
import wooteco.chess.service.SpringChessService;

import java.sql.SQLException;

@Controller
public class SpringChessController {
    @Autowired
    private SpringChessService springChessService;

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("normalStatus", NormalStatus.YES.isNormalStatus());
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView startNewGame() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("chess");
        modelAndView.addObject("normalStatus", NormalStatus.YES.isNormalStatus());
        springChessService.clearHistory();
        return modelAndView;
    }
}
