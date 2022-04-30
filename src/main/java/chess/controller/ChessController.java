package chess.controller;

import chess.dto.StateDto;
import chess.dto.StatusDto;
import chess.service.StateService;
import chess.domain.game.Player;
import chess.domain.state.State;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChessController {

    private final StateService stateService;

    public ChessController(StateService stateService) {
        this.stateService = stateService;
    }

    @PostMapping("/chess/start")
    public String startChessGame(RedirectAttributes redirectAttributes) {
        stateService.initCommandRecord();
        redirectAttributes.addAttribute("message", "게임을 시작합니다.");
        return "redirect:/chess";
    }

    @GetMapping(path = "/chess")
    public ModelAndView printCurrentBoard(@RequestParam String message) {
        State state = stateService.currentState();
        StateDto stateDto = StateDto.of(state);
        ModelAndView modelAndView = new ModelAndView(getViewName(state));
        modelAndView.addObject("squares", stateDto.getSquares());
        modelAndView.addObject("player", stateDto.getPlayer());
        modelAndView.addObject("commands", stateService.getAllCommand());
        modelAndView.addObject("message", message);
        return modelAndView;
    }

//TODO: url 수정
//    @PostMapping(path = "/chess")
//    public String movePiece(RedirectAttributes redirectAttributes, @RequestParam("command") String command) {
//        stateService.currentState()
//                .proceed(command);
//        stateService.insertCommand(command);
//        redirectAttributes.addAttribute("message", command);
//        return "redirect:chess";
//    }

    @GetMapping(path = "/chess/result")
    public ModelAndView printResult() {
        ModelAndView modelAndView = new ModelAndView("status");
        StatusDto statusDto = StatusDto.of(stateService.currentState().proceed("status"));
        modelAndView.addObject("squares", statusDto.getSquares());
        modelAndView.addObject("whiteScore", statusDto.getScore(Player.WHITE));
        modelAndView.addObject("blackScore", statusDto.getScore(Player.BLACK));
        return modelAndView;
    }

    private String getViewName(State state) {
        if (state.isRunning()) {
            return "chess";
        }
        return "finished";
    }
}
