package chess.controller;

import chess.dto.BoardDto;
import chess.dto.BoardStatusDto;
import chess.dto.MovablePositionDto;
import chess.dto.MoveRequestDto;
import chess.service.SpringChessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/start")
    public String startGame(@RequestParam("room") String id, Model model) {
        model.addAttribute("roomId", id);
        return "index";
    }

    @GetMapping("/clear/{id}")
    public String clear(@PathVariable String id) {
        springChessService.deleteRoom(id);
        return "redirect:/";
    }
}
