package chess.application.web.controller;

import chess.application.web.dao.CommandDao;
import chess.application.web.dto.StateDto;
import chess.application.web.dto.StatusDto;
import chess.game.Player;
import chess.state.Start;
import chess.state.State;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChessController {

    private final CommandDao commandDao;

    public ChessController(CommandDao commandDao) {
        this.commandDao = commandDao;
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @PostMapping("/start")
    public String startChessGame(RedirectAttributes redirectAttributes) {
        commandDao.clear();
        redirectAttributes.addAttribute("message", "게임을 시작합니다.");
        return "redirect:game";
    }

    @GetMapping(path = "/game")
    public ModelAndView printCurrentBoard(@RequestParam("message") String message) {
        State state = currentState();
        StateDto stateDto = StateDto.of(state);
        ModelAndView modelAndView = new ModelAndView(getViewName(state));
        modelAndView.addObject("squares", stateDto.getSquares());
        modelAndView.addObject("player", stateDto.getPlayer());
        modelAndView.addObject("commands", commandDao.findAll());
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @PostMapping(path = "/game")
    public String movePiece(RedirectAttributes redirectAttributes, @RequestParam("command") String command) {
        currentState().proceed(command);
        commandDao.insert(command);
        redirectAttributes.addAttribute("message", "실행한 명령어: " + command);
        return "redirect:game";
    }

    @GetMapping(path = "/result")
    public ModelAndView printResult() {
        ModelAndView modelAndView = new ModelAndView("status");
        StatusDto statusDto = StatusDto.of(currentState().proceed("status"));
        modelAndView.addObject("squares", statusDto.getSquares());
        modelAndView.addObject("whiteScore", statusDto.getScore(Player.WHITE));
        modelAndView.addObject("blackScore", statusDto.getScore(Player.BLACK));
        return modelAndView;
    }

    private State currentState() {
        State state = Start.of();
        for (String command : commandDao.findAll()) {
            state = state.proceed(command);
        }
        return state;
    }

    private String getViewName(State state) {
        if (state.isRunning()) {
            return "game";
        }
        return "finished";
    }
}
