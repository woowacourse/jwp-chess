package chess.controller;

import chess.domain.dto.move.MoveRequestDto;
import chess.domain.dto.move.MoveResponseDto;
import chess.serivce.chess.ChessService;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessController {

    private final ChessService service;

    @Autowired
    public ChessController(final ChessService service) {
        this.service = service;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity exceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String welcomeAsHTML() {
        return "index.html";
    }

    @GetMapping(value = "/createroom/{name}")
    @ResponseBody
    public ResponseEntity createRoom(@PathVariable("name") String roomName) throws SQLException {
        service.createRoom(roomName);
        return ResponseEntity.ok().body(roomName);
    }

    @GetMapping(value = "/room/{name}")
    @ResponseBody
    public ResponseEntity enterRoom(@PathVariable("name") String roomName) throws SQLException {
        MoveResponseDto result = service.findPiecesByRoomName(roomName);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/room/{name}/start")
    @ResponseBody
    public ResponseEntity startRoom(@PathVariable("name") String roomName) throws SQLException {
        MoveResponseDto result = service.start(roomName);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/room/{name}/end")
    @ResponseBody
    public ResponseEntity endRoom(@PathVariable("name") String roomName) throws SQLException {
        MoveResponseDto result = service.end(roomName);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/room/{name}/move")
    @ResponseBody
    public ResponseEntity move(@PathVariable("name") String roomName, @RequestBody MoveRequestDto moveRequestDto)
            throws SQLException {
        MoveResponseDto result = service.move(roomName, moveRequestDto.getSource(),
                moveRequestDto.getTarget());
        return ResponseEntity.ok().body(result);
    }
}
