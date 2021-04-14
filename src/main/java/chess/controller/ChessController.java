package chess.controller;

import chess.domain.dto.move.MoveRequestDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String welcomeAsHTML() {
        return "index.html";
    }

    @GetMapping(value = "/createroom/{name}")
    @ResponseBody
    public ResponseEntity createRoom(@PathVariable("name") String roomName) {
        return ResponseEntity.ok().body(roomName);
    }

    @GetMapping(value = "/room/{name}")
    @ResponseBody
    public ResponseEntity enterRoom(@PathVariable("name") String roomName) {
        return ResponseEntity.ok().body(roomName);
    }

    @GetMapping(value = "/room/{name}/start")
    @ResponseBody
    public ResponseEntity startRoom(@PathVariable("name") String roomName) {
        return ResponseEntity.ok().body(roomName);
    }

    @GetMapping(value = "/room/{name}/end")
    @ResponseBody
    public ResponseEntity endRoom(@PathVariable("name") String roomName) {
        return ResponseEntity.ok().body(roomName);
    }

    @PostMapping(value = "/room/{name}/move")
    @ResponseBody
    public ResponseEntity move(@PathVariable("name") String roomName, @RequestBody MoveRequestDto moveRequestDto) {
        return ResponseEntity.ok().body(moveRequestDto);
    }
}
