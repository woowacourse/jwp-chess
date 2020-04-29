package wooteco.chess.controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.domain.player.User;
import wooteco.chess.service.ChessService;

@Controller
public class ChessController {

    private ChessService chessService;

    ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return createEmptyModelAndView();
    }

    @PostMapping("/start")
    public ModelAndView start(@RequestParam HashMap<String, String> paramMap)
            throws SQLException {
        ModelAndView modelAndView = new ModelAndView();

        User user = new User(paramMap.get("userName"));
        modelAndView.addObject("user", user.getName());
        modelAndView.addObject("rows", chessService.getRowsDto(user));
        modelAndView.addObject("turn", chessService.getTurn(user));

        modelAndView.setViewName("board");

        return modelAndView;
    }

    @PostMapping("/path")
    @ResponseBody
    public List<String> path(@RequestParam HashMap<String, String> paramMap) {
        try {
            return chessService.searchPath(new User(paramMap.get("userName")), paramMap.get("source"));
        } catch (RuntimeException e) {
            return Collections.singletonList(e.getMessage());
        }
    }

    @PostMapping("/move")
    @ResponseBody
    public Map<String, Object> move(@RequestParam HashMap<String, String> paramMap) {
        User blackUser = new User(paramMap.get("userName"));

        Map<String, Object> model = new HashMap<>();
        model.put("isNotFinished", false);
        model.put("message", "");

        try {
            chessService.move(blackUser, paramMap.get("source"), paramMap.get("target"));
            model.put("white", chessService.calculateWhiteScore(blackUser));
            model.put("black", chessService.calculateBlackScore(blackUser));
        } catch (RuntimeException e) {
            model.put("message", e.getMessage());
        }
        if (chessService.checkGameNotFinished(blackUser)) {
            model.put("isNotFinished", true);
        }
        return model;
    }

    @PostMapping("/save")
    public ModelAndView save(@RequestParam String userName)
            throws SQLException {
        chessService.save(new User(userName));

        return createEmptyModelAndView();
    }

    @PostMapping("/end")
    public ModelAndView end(@RequestParam String userName)
            throws SQLException {
        chessService.delete(new User(userName));

        return createEmptyModelAndView();
    }

    private ModelAndView createEmptyModelAndView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("rows", chessService.getEmptyRowsDto());
        modelAndView.setViewName("main");
        return modelAndView;
    }
}
