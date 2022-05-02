package chess.controller;

import chess.service.dto.ChessRequestDto;
import chess.service.dto.PasswordRequestDto;
import chess.domain.command.MoveCommand;
import chess.service.dto.ChessResponseDto;
import chess.controller.dto.MoveCommandDto;
import chess.service.dto.RoomResponseDto;
import chess.service.dto.ScoresDto;
import chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chess-game")
public class ChessController {

    private final ChessService chessService;

    @Autowired
    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/pieces/{id}")
    public ChessResponseDto getPieces(@PathVariable int id) {
        return chessService.getChess(id);
    }

    @GetMapping("/rooms")
    public List<RoomResponseDto> getRooms() {
        return chessService.getChesses();
    }

    @GetMapping("/score/{id}")
    public ScoresDto score(@PathVariable int id) {
        return chessService.getScore(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public int start(@RequestBody ChessRequestDto chessRequestDto) {
        return chessService.start(chessRequestDto);
    }

    @PutMapping(value = "/move/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChessResponseDto move(@PathVariable int id, @RequestBody MoveCommandDto moveCommandDto) {
        MoveCommand moveCommand = moveCommandDto.toEntity();
        return chessService.movePiece(id, moveCommand);
    }

    @PutMapping("/end/{id}")
    public ScoresDto end(@PathVariable int id) {
        return chessService.finishGame(id);
    }

    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable int id, @RequestBody PasswordRequestDto passwordRequestDto) {
        chessService.remove(id, passwordRequestDto);
    }
}
