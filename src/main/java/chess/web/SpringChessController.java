package chess.web;

import chess.web.dao.BoardDao;
import chess.web.dao.PieceDao;
import chess.web.dto.RoomDto;
import chess.web.service.GameService;
import chess.web.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpringChessController {

    private RoomService roomService;
    private GameService gameService;

    public SpringChessController(RoomService roomService) {
        this.roomService = roomService;
        this.gameService = new GameService(new PieceDao(), new BoardDao());
    }

    @GetMapping("/")
    public String login() {
        return "login.html";
    }

    @PostMapping(value = "/board")
    public String createRoom(@RequestParam String name) {
        RoomDto roomDto = roomService.create(name);
        return "redirect:/board?roomId=" + roomDto.getId();
    }

    @GetMapping("/board")
    public String board() {
        return "/board.html";
    }

    @GetMapping("/start")
    public ResponseEntity startNewGame(@RequestParam int roomId) {
        return ResponseEntity.ok(gameService.startNewGame(roomId));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity validateName() {
        return ResponseEntity.badRequest().build();
    }
}
