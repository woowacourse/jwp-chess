package chess.controller;

import chess.model.dto.GameInfosDto;
import chess.model.dto.RoomDto;
import chess.model.dto.WebBoardDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChessHomeController {

    private final ChessService chessService;

    public ChessHomeController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/new")
    public Map<String, String> startNewGame(@RequestBody RoomDto roomDto) {
        WebBoardDto board = chessService.start(roomDto);
        return board.getWebBoard();
        // 생각해볼 부분: ResponseEntity의 사용
    }

    @GetMapping("/games")
    public ResponseEntity<GameInfosDto> getAllGames() {
        return ResponseEntity.ok(chessService.getAllGames());
    }
}
