package chess.controller;

import chess.dto.DeleteResponseDto;
import chess.service.SpringChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessRestController {
    private final SpringChessService springChessService;

    public SpringChessRestController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @DeleteMapping(value = "/delete/{roomName}")
    public ResponseEntity<DeleteResponseDto> delete(@PathVariable("roomName") String roomName) {
        springChessService.deleteRoom(roomName);
        DeleteResponseDto deleteResponseDto = new DeleteResponseDto(roomName, true);

        return ResponseEntity.ok().body(deleteResponseDto);
    }
}
