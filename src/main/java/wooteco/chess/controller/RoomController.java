package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import wooteco.chess.entity.Room;
import wooteco.chess.service.ChessService;

@Controller
public class RoomController {

    private ChessService chessService;

    RoomController(ChessService chessService) {
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
