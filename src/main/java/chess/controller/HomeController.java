package chess.controller;

import chess.controller.request.RoomCreationRequest;
import chess.controller.request.RoomDeletionRequest;
import chess.controller.response.RoomResponse;
import chess.dto.GameDto;
import chess.serviece.ChessGameService;
import chess.serviece.dto.PasswordDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ChessGameService chessGameService;

    public HomeController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping
    public String init() {
        return "home";
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomResponse>> getRooms() {
        List<GameDto> games = chessGameService.getAllGames();
        List<RoomResponse> roomResponses = games.stream()
                .map(RoomResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(roomResponses);
    }

    @PostMapping(value = "/rooms")
    public ResponseEntity<Long> addRoom(@RequestBody RoomCreationRequest roomCreationRequest) {
        return ResponseEntity.ok().body(chessGameService.addGame(roomCreationRequest));
    }

    @DeleteMapping(value = "/rooms/{id}")
    public ResponseEntity<Long> deleteRoom(@PathVariable Long id, @RequestBody RoomDeletionRequest roomDeletionRequest) {
        chessGameService.deleteGame(id, new PasswordDto(roomDeletionRequest.getPassword()));
        return ResponseEntity.noContent().build();
    }
}
