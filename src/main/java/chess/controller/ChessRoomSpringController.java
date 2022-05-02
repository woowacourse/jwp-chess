package chess.controller;

import chess.domain.ChessGameService;
import chess.dto.DeleteDto;
import chess.dto.GameStatusDto;
import chess.dto.RoomResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessRoomSpringController {

    private final ChessGameService chessGameService;

    public ChessRoomSpringController(ChessGameService chessGameService) {
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
    public ResponseEntity<GameStatusDto> deleteRoom(@RequestBody DeleteDto deleteDto) {
        chessGameService.deleteRoom(deleteDto.getPw(), deleteDto.getId());
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity<List<RoomResponseDto>> list() {
        List<RoomResponseDto> rooms = chessGameService.getRooms();
        return ResponseEntity.ok().body(rooms);
    }
}
