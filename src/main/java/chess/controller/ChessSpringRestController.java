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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PatchMapping("/boards/{id}/move")
    public ResponseEntity<Boolean> move(@PathVariable Long id, @RequestBody String moveRequest) {
        List<String> command = Arrays.asList(moveRequest.split(" "));
        chessGameService.move(id, command.get(0), command.get(1));
        final boolean gameEnd = chessGameService.isGameEnd(id);
        return ResponseEntity.ok().body(gameEnd);
    }

    @PostMapping("/boards/{id}/end")
    public ResponseEntity<String> end(@PathVariable Long id) {
        chessGameService.end(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/boards/{id}/restart")
    public ResponseEntity<String> restart(@PathVariable Long id) {
        chessGameService.restart(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/boards/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestBody String password) {
        chessGameService.delete(id, password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
