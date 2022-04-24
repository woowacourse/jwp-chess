package chess.controller;

import chess.domain.Command;
import chess.domain.piece.Team;
import chess.service.ChessService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringController {

    private final ChessService chessService;

    @Autowired
    public SpringController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/start")
    public String start(@RequestParam("game_name") String gameName) {
        return "redirect:/game/" + gameName;
    }

    @GetMapping("/game/{gameName}")
    public String game(@PathVariable String gameName, Model model) {
        List<String> chessBoard = chessService.findByName(gameName);
        model.addAttribute("chessboard", chessBoard);

        return "chess";
    }

    @PostMapping("/game/{gameName}/move")
    public String move(@PathVariable String gameName,
                             @RequestParam("from") String from, @RequestParam("to") String to,
                       Model model) {
        try {
            String command = makeCommand(from, to);
            chessService.move(command);
            if (chessService.isEnd()) {
                return "redirect:/end";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/game/" + gameName;
    }

    @GetMapping("/game/{game_name}/end")
    public ModelAndView end() {
        String winTeamName = chessService.finish(Command.from("end"));
        List<String> chessBoard = chessService.getCurrentChessBoard();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("winTeam", winTeamName);
        modelAndView.addObject("chessboard", chessBoard);
        modelAndView.setViewName("chess");

        return modelAndView;
    }

    @GetMapping("/save")
    public ModelAndView save() {
        ModelAndView modelAndView = new ModelAndView();
        try {
            chessService.save();
        } catch (IllegalStateException e) {
            modelAndView.setViewName("redirect:/end");
            return modelAndView;
        }

        List<String> chessBoard = chessService.getCurrentChessBoard();
        modelAndView.addObject("chessboard", chessBoard);
        modelAndView.setViewName("chess");

        return modelAndView;
    }

    @GetMapping("/status")
    public ModelAndView status() {
        ModelAndView modelAndView = new ModelAndView();

        Map<Team, Double> score = chessService.getScore();
        List<String> chessBoard = chessService.getCurrentChessBoard();

        modelAndView.addObject("blackScore", score.get(Team.BLACK));
        modelAndView.addObject("whiteScore", score.get(Team.WHITE));
        modelAndView.addObject("chessboard", chessBoard);
        modelAndView.setViewName("chess");

        return modelAndView;
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    private String makeCommand(String from, String to) {
        return "move " + from + " " + to;
    }
}
