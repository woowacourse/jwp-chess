package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.DeleteRequestDto;
import chess.dto.DeleteResponseDto;
import chess.dto.MoveDto;
import chess.dto.RoomDto;
import chess.service.ChessService;
import chess.view.WebInputValicator;
import com.google.gson.Gson;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {
    private final ChessService chessService;

    public WebController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String showFirstPage() {
        return "roomlist";
    }

    @GetMapping("/games")
    public ResponseEntity<List<RoomDto>> showChessRoomListPage() {
        return ResponseEntity.ok().body(chessService.loadRooms());
    }

    @DeleteMapping("/games")
    public ResponseEntity<DeleteResponseDto> delete(@RequestBody DeleteRequestDto deleteRequestDto) {
        return ResponseEntity.ok().body(chessService.deleteGame(deleteRequestDto));
    }

    @GetMapping("/game/{id}")
    public String showBoard() {
        return "room";
    }

    @GetMapping("/start/{id}")
    public ResponseEntity<ChessGameDto> startGame(@PathVariable int id) {
        return ResponseEntity.ok().body(chessService.newGame(id));
    }

    @GetMapping("/restart/{id}")
    public ResponseEntity<ChessGameDto> restart(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(chessService.loadGame(id));
    }

    @PutMapping("/move/{id}")
    public ResponseEntity<ChessGameDto> move(@PathVariable("id") int id, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok().body(chessService.move(id, moveDto.getFrom(), moveDto.getTo()));
    }

    @GetMapping("/endGame/{id}")
    public ResponseEntity<ChessGameDto> endGame(@PathVariable("id") int id) {
        chessService.endGame(id);
        return ResponseEntity.ok().body(chessService.loadGame(id));
    }

    @GetMapping("/join")
    public String showJoinPage() {
        return "join";
    }

    @PostMapping("/roomInformation")
    public String createRoom(@RequestParam(value="name") String name,
                             @RequestParam(value="password") String password) {
        WebInputValicator.checkRoomName(name);
        WebInputValicator.checkRoomPassword(password);
        chessService.createRoom(name, password);
        return "redirect:/";
    }

    @ExceptionHandler({RuntimeException.class})
    private ResponseEntity<String> handleException(final RuntimeException exception) {
        Gson gson = new Gson();
        return ResponseEntity.badRequest().body(gson.toJson(exception.getMessage()));
    }
}
