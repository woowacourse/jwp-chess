package chess.controller.web;

import chess.dto.ChessGameDto;
import chess.dto.RoomDto;
import chess.service.SpringChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class ChessViewController {
    private final SpringChessService springChessService;

    public ChessViewController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @PostMapping("/create")
    public String createRoom(@ModelAttribute("roomName") String roomName) {
        int roomNo = springChessService.createRoom(roomName);
        return "redirect:/rooms/" + roomNo;
    }

    @GetMapping("/{roomNo}")
    public ModelAndView enterRoom(@PathVariable("roomNo") int roomNo) {
        ChessGameDto chessGameDto = springChessService.loadRoom(roomNo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roomNo", roomNo);
        modelAndView.addObject("roomName", chessGameDto.getRoomName());
        modelAndView.setViewName("game");
        return modelAndView;
    }

    @GetMapping
    public ModelAndView showRooms() {
        List<RoomDto> rooms = springChessService.getAllSavedRooms();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("rooms", rooms);
        modelAndView.setViewName("repository");
        return modelAndView;
    }

    @GetMapping("/load")
    public String loadRoom(@RequestParam("roomNo") int roomNo) {
        return "redirect:/rooms/" + roomNo;
    }
}