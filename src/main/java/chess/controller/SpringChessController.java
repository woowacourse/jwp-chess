package chess.controller;

import chess.service.SpringChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    @PostMapping("/rooms")
    public String createGame(@RequestParam("room") String name) {
        String id = springChessService.createRoom(name);
        return "redirect:/rooms/" + id;
    }

    @GetMapping("/rooms/{id}")
    public ModelAndView enterRoom(@PathVariable String id) {
        final ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("roomId", id);
        return modelAndView;
    }

    @DeleteMapping("/rooms/{id}/commands")
    public ResponseEntity<Void> clear(@PathVariable String id) {
        springChessService.resetRoom(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
        springChessService.deleteRoom(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
