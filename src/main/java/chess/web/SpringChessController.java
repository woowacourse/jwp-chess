package chess.web;

import chess.web.dao.BoardDao;
import chess.web.dao.PieceDao;
import chess.web.dto.CommendDto;
import chess.web.dto.RoomDto;
import chess.web.service.GameService;
import chess.web.service.RoomService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/load")
    public ResponseEntity loadGame(@RequestParam int roomId) {
        return ResponseEntity.ok(gameService.loadGame(roomId));
    }

    @PostMapping("/move")
    public ResponseEntity movePiece(@RequestParam int boardId, @RequestBody CommendDto commendDto) {
        gameService.move(boardId, commendDto);
        return ResponseEntity.ok(gameService.gameStateAndPieces(boardId));
    }

    @GetMapping("/result")
    public ResponseEntity result(@RequestParam int boardId) {
        return ResponseEntity.ok(gameService.gameResult(boardId));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleBadRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
    }
}
