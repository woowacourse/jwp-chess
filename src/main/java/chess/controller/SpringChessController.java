package chess.controller;

import chess.dto.RoomDto;
import chess.service.SpringChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String mainPage() {
        return "main";
    }

    @GetMapping("/create")
    public String startGame(@RequestParam("room") String name) {
        String id = springChessService.createRoom(name);
        return "redirect:/enter/" + id;
    }

    @GetMapping("/enter/{id}")
    public ModelAndView enterRoom(@PathVariable String id) {
        final ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("roomId", id);
        return modelAndView;
    }

    @GetMapping("/clear/{id}")
    public String clear(@PathVariable String id) {
        springChessService.resetRoom(id);
        return "redirect:/";
    }

    @GetMapping("/list")
    public ResponseEntity<List<RoomDto>> isExistingRoom() {
        return ResponseEntity.ok(springChessService.roomIds());
    }
}
