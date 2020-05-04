package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.chess.db.MoveHistory;
import wooteco.chess.service.SpringChessService;

@Controller
public class SpringChessController {
    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/")
    public String chessGame(Model model) {
        model.addAttribute("rooms", springChessService.findRooms());
        return "index";
    }

    @PostMapping("/play")
    public String startGame(@RequestParam(value = "room_name") String roomName, Model model) {
        Long roomId = springChessService.startNewGame(roomName);

        model.addAllAttributes(springChessService.provideGameInfo(roomId));
        model.addAttribute("room_id", roomId);
        return "game_room";
    }

    @PostMapping("/resume")
    public String resumeGame(@RequestParam(value = "room_id") Long roomId, Model model) {
        springChessService.resumeGame(roomId);

        model.addAllAttributes(springChessService.provideGameInfo(roomId));
        model.addAttribute("room_id", roomId);
        return "game_room";
    }

    @PostMapping("/move")
    public String move(@RequestParam(value = "room_id") Long roomId, MoveHistory moveHistory, Model model) {
        springChessService.move(roomId, moveHistory);

        model.addAllAttributes(springChessService.provideGameInfo(roomId));
        model.addAttribute("room_id", roomId);
        model.addAttribute("end", springChessService.provideWinner(roomId));
        return "game_room";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String errorMessage(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "game_room";
    }
}
