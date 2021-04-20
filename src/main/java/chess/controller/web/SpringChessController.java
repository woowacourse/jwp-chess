package chess.controller.web;

import chess.dto.ChessGameDto;
import chess.dto.RoomDto;
import chess.service.SpringChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SpringChessController {
    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/create")
    public String createRoom(@RequestParam("roomName") String roomName) {
        int roomNo = springChessService.createRoom(roomName);
        return "redirect:/room/" + roomNo;
    }

    @GetMapping("/room/{roomNo}")
    public ModelAndView enterRoom(@PathVariable("roomNo") int roomNo) {
        ChessGameDto chessGameDto = springChessService.loadRoom(roomNo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roomNo", roomNo);
        modelAndView.addObject("roomName", chessGameDto.getRoomName());
        modelAndView.setViewName("game");
        return modelAndView;
    }

    @GetMapping(value = "/rooms")
    public ModelAndView showRooms() {
        List<RoomDto> rooms = springChessService.getAllSavedRooms();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("rooms", rooms);
        modelAndView.setViewName("repository");
        return modelAndView;
    }

    @GetMapping("/load")
    public String loadRoom(@RequestParam("roomNo") int roomNo) {
        return "redirect:/room/" + roomNo;
    }
}
