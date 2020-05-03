package wooteco.chess.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.entity.Room;
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
    public String start(@RequestParam String roomName) {
        return "redirect:/start/" + roomName;
    }

    @GetMapping("/start/{room}")
    public ModelAndView room(@PathVariable("room") String roomName) {
        ModelAndView modelAndView = new ModelAndView();

        Room room = new Room(roomName);
        modelAndView.addObject("room", room.getName());
        modelAndView.addObject("rows", chessService.getRowsDto(room));
        modelAndView.addObject("turn", chessService.getTurn(room));

        modelAndView.setViewName("board");

        return modelAndView;
    }

    @PostMapping("/path")
    @ResponseBody
    public List<String> path(@RequestParam Map<String, String> parameters) {
        try {
            return chessService.searchPath(new Room(parameters.get("roomName")), parameters.get("source"));
        } catch (RuntimeException e) {
            return Collections.singletonList(e.getMessage());
        }
    }

    @PostMapping("/move")
    @ResponseBody
    public Map<String, Object> move(@RequestParam Map<String, String> parameters) {
        Room blackRoom = new Room(parameters.get("roomName"));

        Map<String, Object> model = new HashMap<>();
        model.put("isNotFinished", false);
        model.put("message", "");

        try {
            chessService.move(blackRoom, parameters.get("source"), parameters.get("target"));
            model.put("white", chessService.calculateWhiteScore(blackRoom));
            model.put("black", chessService.calculateBlackScore(blackRoom));
        } catch (RuntimeException e) {
            model.put("message", e.getMessage());
        }
        if (chessService.checkGameNotFinished(blackRoom)) {
            model.put("isNotFinished", true);
        }
        return model;
    }

    @PostMapping("/save")
    public ModelAndView save(@RequestParam String roomName) {
        chessService.save(new Room(roomName));

        return createEmptyModelAndView();
    }

    @PostMapping("/end")
    public ModelAndView end(@RequestParam String roomName) {
        chessService.delete(new Room(roomName));

        return createEmptyModelAndView();
    }

    private ModelAndView createEmptyModelAndView() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("rows", chessService.getEmptyRowsDto());
        modelAndView.addObject("rooms", chessService.getRooms());
        modelAndView.setViewName("main");

        return modelAndView;
    }
}
