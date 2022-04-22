package chess.controller;

import chess.domain.Command;
import chess.domain.piece.Team;
import chess.service.ChessService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ModelAndView start(@RequestParam("game_name") String gameName) {
        List<String> chessBoard = chessService.findByName(gameName);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("chessboard", chessBoard);
        modelAndView.setViewName("chess");

        return modelAndView;
    }

    @PostMapping("/move")
    public ModelAndView move(@RequestParam("from") String from, @RequestParam("to") String to) {
        List<String> chessBoard = chessService.getCurrentChessBoard();

        ModelAndView modelAndView = new ModelAndView();
        try {
            String command = makeCommand(from, to);
            chessBoard = chessService.move(command);
            if (chessService.isEnd()) {
                modelAndView.setViewName("redirect:/end");
                return modelAndView;
            }
        } catch (IllegalArgumentException e) {
            modelAndView.addObject("error", e.getMessage());
        }

        modelAndView.addObject("chessboard", chessBoard);
        modelAndView.setViewName("chess");
        return modelAndView;
    }

    @GetMapping("/end")
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

    private String makeCommand(String from, String to) {
        return "move " + from + " " + to;
    }
}
