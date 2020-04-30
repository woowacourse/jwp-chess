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
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("rows", chessService.getEmptyRowsDto());
        modelAndView.setViewName("main");
        return modelAndView;
    }

    @PostMapping("/start")
    public ModelAndView start(@RequestParam HashMap<String, String> paramMap) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();

        User blackUser = new User(paramMap.get("blackUserName"));
        User whiteUser = new User(paramMap.get("whiteUserName"));

        modelAndView.addObject("blackUser", blackUser.getName());
        modelAndView.addObject("whiteUser", whiteUser.getName());
        modelAndView.addObject("rows", chessService.getRowsDto(blackUser, whiteUser));
        modelAndView.addObject("turn", chessService.getTurn(blackUser));

        modelAndView.setViewName("board");

        return modelAndView;
    }

    @PostMapping("/path")
    @ResponseBody
    public List<String> path(@RequestParam HashMap<String, String> paramMap) {
        try {
            return chessService.searchPath(new User(paramMap.get("blackUserName")), paramMap.get("source"));
        } catch (RuntimeException e) {
            return Collections.singletonList(e.getMessage());
        }
    }

    // todo: 죽일 수 있는 말 표시 확 인
    @PostMapping("/move")
    @ResponseBody
    public Map<String, Object> move(@RequestParam HashMap<String, String> paramMap) {
        User blackUser = new User(paramMap.get("blackUserName"));

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
    public ModelAndView save(@RequestParam HashMap<String, String> paramMap) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();

        chessService.save(new User(paramMap.get("blackUserName")), new User(paramMap.get("whiteUserName")));

        modelAndView.addObject("rows", chessService.getEmptyRowsDto());
        modelAndView.setViewName("main");
        return modelAndView;
    }

    @PostMapping("/end")
    public ModelAndView end(@RequestParam HashMap<String, String> paramMap) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();

        chessService.delete(new User(paramMap.get("blackUserName")), new User(paramMap.get("whiteUserName")));

        modelAndView.addObject("rows", chessService.getEmptyRowsDto());
        modelAndView.setViewName("main");
        return modelAndView;
    }
}
