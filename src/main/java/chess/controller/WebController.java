package chess.controller;

import chess.dto.DeleteRequestDto;
import chess.dto.DeleteResponseDto;
import chess.dto.MoveDto;
import chess.dto.RoomDto;
import chess.service.ChessService;
import com.google.gson.Gson;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class WebController {

    private final Gson gson = new Gson();
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
    @ResponseBody
    public String startGame(@PathVariable("id") int id) {
        return gson.toJson(chessService.newGame(id));
    }

    @GetMapping("/restart/{id}")
    @ResponseBody
    public String restart(@PathVariable("id") int id) {
        return gson.toJson(chessService.loadGame(id));
    }

    @PutMapping("/move/{id}")
    @ResponseBody
    public String move(@PathVariable("id") int id, @RequestBody MoveDto moveDto) {
        return gson.toJson(chessService.move(id, moveDto.getFrom(), moveDto.getTo()));
    }

    @GetMapping("/endGame/{id}")
    @ResponseBody
    public String endGame(@PathVariable("id") int id) {
        chessService.endGame(id);
        return gson.toJson(chessService.loadGame(id));
    }

    @GetMapping("/join")
    public String showJoinPage() {
        return "join";
    }

    @PostMapping("/roomInformation")
    public String createRoom(@RequestParam(value="name") String name,
                             @RequestParam(value="password") String password) {
        chessService.createRoom(name, password);
        return "redirect:/";
    }

    /*@GetMapping("/restart")
    @ResponseBody
    public String restart() {
        return gson.toJson(chessService.loadGame());
    }*/

    /*@PutMapping("/move")
    @ResponseBody
    public String move2(@RequestBody MoveDto moveDto) {
        return gson.toJson(chessService.move(moveDto.getFrom(), moveDto.getTo()));
    }*/

    @ExceptionHandler({RuntimeException.class})
    private ResponseEntity<String> handleException(final RuntimeException exception) {
        ResponseEntity.status(500);
        return ResponseEntity.badRequest().body(gson.toJson(exception.getMessage()));
    }
}
