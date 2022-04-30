package chess.controller;

import chess.dto.StateDto;
import chess.dto.StatusDto;
import chess.service.CommandService;
import chess.domain.game.Player;
import chess.domain.state.State;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessController {

    private final CommandService commandService;

    public ChessController(CommandService commandService) {
        this.commandService = commandService;
    }

    @GetMapping(path = "/chess/{id}")
    public ModelAndView printCurrentBoard(@PathVariable("id") Long id, @RequestParam(required = false) String message) {
        List<String> commands = commandService.findAllByRoomID(id);
        State state = commandService.getCurrentState(commands);
        StateDto stateDto = StateDto.of(commandService.getCurrentState(commands));
        ModelAndView modelAndView = new ModelAndView(getViewName(state));
        modelAndView.addObject("squares", stateDto.getSquares());
        modelAndView.addObject("player", stateDto.getPlayer());
        modelAndView.addObject("commands", commands);
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

    @GetMapping(path = "/chess/{id}/result")
    public ModelAndView printResult(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("status");
        StatusDto statusDto = StatusDto.of(commandService.getCurrentState(commandService.findAllByRoomID(id))
                .proceed("status"));
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
