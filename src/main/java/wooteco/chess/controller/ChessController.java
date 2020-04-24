package wooteco.chess.controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wooteco.chess.domain.player.User;
import wooteco.chess.service.ChessService;

@Controller
public class ChessController {

    private ChessService chessService;

    ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("rows", chessService.getEmptyRowsDto());
        return "main";
    }

    @PostMapping("/start")
    public String start(Model model, @RequestParam String blackUserName,
        @RequestParam String whiteUserName) throws SQLException {

        User blackUser = new User(blackUserName);
        User whiteUser = new User(whiteUserName);

        model.addAttribute("blackUser", blackUserName);
        model.addAttribute("whiteUser", whiteUserName);
        model.addAttribute("rows", chessService.getRowsDto(blackUser, whiteUser));
        model.addAttribute("turn", chessService.getTurn(blackUser));

        return "board";
    }

    @PostMapping("/path")
    @ResponseBody
    public List<String> path(@RequestParam String source, @RequestParam String blackUserName) {
        try {
            return chessService.searchPath(new User(blackUserName), source);
        } catch (RuntimeException e) {
            return Collections.singletonList(e.getMessage());
        }
    }

    // todo: front로 넘어가는 과정 이해할 것
    @PostMapping("/move")
    @ResponseBody
    public Map<String, Object> move(@RequestParam String source, @RequestParam String target, @RequestParam String blackUserName) {
        User blackUser = new User(blackUserName);

        Map<String, Object> model = new HashMap<>();
        model.put("status", false);
        model.put("message", "");

        try {
            chessService.move(blackUser, source, target);
            model.put("white", chessService.calculateWhiteScore(blackUser));
            model.put("black", chessService.calculateBlackScore(blackUser));
        } catch (RuntimeException e) {
            model.put("message", e.getMessage());
        }
        if (chessService.checkGameNotFinished(blackUser)) {
            model.put("status", true);
        }
        return model;
    }

    @PostMapping("/save")
    public String save(Model model, @RequestParam String blackUserName, @RequestParam String whiteUserName)
        throws SQLException {

        chessService.save(new User(blackUserName), new User(whiteUserName));

        model.addAttribute("rows", chessService.getEmptyRowsDto());
        return "redirect:/";
    }

    @PostMapping("/end")
    public String end(Model model, @RequestParam String blackUserName, @RequestParam String whiteUserName)
        throws SQLException {

        chessService.delete(new User(blackUserName), new User(whiteUserName));
        model.addAttribute("rows", chessService.getEmptyRowsDto());
        return "redirect:/";
    }
}
