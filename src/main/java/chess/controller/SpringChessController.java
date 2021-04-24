package chess.controller;

import chess.dto.RoomDto;
import chess.service.SpringChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SpringChessController {
    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @PostMapping("/room")
    public String createGame(@RequestParam("room") String name) {
        String id = springChessService.createRoom(name);
        return "redirect:/game/" + id;
    }

    @GetMapping("/game/{id}")
    public ModelAndView enterRoom(@PathVariable String id) {
        final ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("roomId", id);
        return modelAndView;
    }

    @DeleteMapping("/clear/{id}")
    public String clear(@PathVariable String id) {
        springChessService.resetRoom(id);
        return "redirect:/";
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> isExistingRoom() {
        return ResponseEntity.ok(springChessService.roomIds());
    }
}
