package chess.controller;

import chess.dto.*;
import chess.service.SpringChessService;
import chess.view.ChessMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.util.List;

@RestController
public class SpringChessGameController {

    private final SpringChessService springChessService;

    public SpringChessGameController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("index", HttpStatus.OK);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> showRooms() {
        final List<RoomDto> roomDtos = springChessService.showRooms();
        return ResponseEntity.ok(roomDtos);
    }

    @PostMapping("/start")
    public ResponseEntity<Long> createGame(@RequestBody RoomRequestDto roomRequestDto) {
        final long roomId = springChessService.makeChessRoom(roomRequestDto);
        return ResponseEntity.created(URI.create("/" + roomId)).body(roomId);
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<ChessMap> load(@PathVariable long id) {
        return ResponseEntity.ok(springChessService.load(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteGame(@PathVariable long id,
                                                    @RequestBody RoomDeleteRequestDto roomDeleteRequestDto) {
        return ResponseEntity.ok(springChessService.deleteChessRoom(roomDeleteRequestDto, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChessMap> start(@PathVariable long id) {
        return ResponseEntity.ok(springChessService.initializeGame(id));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ScoreDto> status(@PathVariable long id) {
        return ResponseEntity.ok(springChessService.getStatus(id));
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<ChessMap> move(@PathVariable long id, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok(springChessService.move(id, moveDto));
    }

    @PatchMapping("/{id}/end")
    public ResponseEntity<ResultDto> end(@PathVariable long id) {
        final ResultDto resultDto = springChessService.getResult(id);
        return ResponseEntity.ok(resultDto);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageDto> handle(Exception e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return ResponseEntity.internalServerError().body(errorMessageDto);
    }
}
