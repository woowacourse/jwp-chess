package chess.controller;

import chess.domain.ChessGameService;
import chess.domain.board.strategy.WebBasicBoardStrategy;
import chess.dto.DeleteDto;
import chess.dto.GameStatusDto;
import chess.dto.MoveDto;
import chess.dto.RoomResponseDto;
import chess.dto.ScoreDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessSpringController {

    private final ChessGameService chessGameService;

    public ChessSpringController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String enter() {
        return "enter";
    }

    @PostMapping("/")
    public String create(@RequestParam("name") String name, @RequestParam("pw") String pw) {
        chessGameService.createRoom(name, pw);
        return "redirect:/";
    }

    @GetMapping("/room")
    public String room(@RequestParam @NonNull int id) {
        return "index";
    }

    @DeleteMapping("/room")
    public ResponseEntity deleteRoom(@RequestBody DeleteDto deleteDto) {
        chessGameService.deleteRoom(deleteDto.getPw(), deleteDto.getId());
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity list() {
        List<RoomResponseDto> rooms = chessGameService.getRooms();
        return ResponseEntity.ok().body(rooms);
    }

    @ResponseBody
    @GetMapping("/rooms/{id}")
    public ResponseEntity load(@PathVariable int id) {
        GameStatusDto status = chessGameService.loadChessGame(id);
        return ResponseEntity.ok().body(status);
    }

    @ResponseBody
    @GetMapping("/rooms/start/{id}")
    public ResponseEntity start(@PathVariable int id) {
        GameStatusDto status = chessGameService.startChessGame(new WebBasicBoardStrategy(), id);
        return ResponseEntity.ok().body(status);
    }

    @ResponseBody
    @PostMapping("/rooms/move")
    public ResponseEntity move(@RequestBody MoveDto moveDto) {
        GameStatusDto status = chessGameService.move(moveDto.getFrom(), moveDto.getTo(), moveDto.getRoomId());
        return ResponseEntity.ok().body(status);
    }

    @ResponseBody
    @GetMapping("/rooms/status/{id}")
    public ResponseEntity status(@PathVariable int id) {
        ScoreDto score = chessGameService.createScore(id);
        return ResponseEntity.ok().body(score);
    }

    @ResponseBody
    @PostMapping("/rooms/end/{id}")
    public ResponseEntity end(@PathVariable int id) {
        ScoreDto score = chessGameService.end(id);
        return ResponseEntity.ok().body(score);
    }
}
