package chess.controller;

import chess.dto.RoomInfoDto;
import chess.service.ChessGameService;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessSpringRestController {
    private final ChessGameService chessGameService;

    public ChessSpringRestController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/")
    public ResponseEntity<String> create(@Valid @RequestBody RoomInfoDto roomInfoDto) {
        chessGameService.create(roomInfoDto.getTitle(), roomInfoDto.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/board/move")
    public ResponseEntity<Boolean> move(@RequestParam(name = "id") Long id, @RequestBody String moveRequest) {
        List<String> command = Arrays.asList(moveRequest.split(" "));
        chessGameService.move(id, command.get(0), command.get(1));
        final boolean gameEnd = chessGameService.isGameEnd(id);
        return ResponseEntity.ok().body(gameEnd);
    }

    @PostMapping("/board/end")
    public ResponseEntity<String> end(@RequestParam(name = "id") Long id) {
        chessGameService.end(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/board/restart")
    public ResponseEntity<String> restart(@RequestParam(name = "id") Long id) {
        chessGameService.restart(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/board")
    public ResponseEntity<String> delete(@RequestParam(name = "id") Long id, @RequestBody String password) {
        chessGameService.delete(id, password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
