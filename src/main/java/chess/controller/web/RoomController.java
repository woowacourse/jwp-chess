package chess.controller.web;

import chess.service.GameService;
import chess.service.RoomService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
public class RoomController {
    private final RoomService roomService;
    private final GameService gameService;

    public RoomController(RoomService roomService, GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @GetMapping("/rooms")
    public String loadRoomList(Model model) {
        model.addAttribute("list", roomService.loadList());
        return "mainPage";
    }

    @PostMapping("/rooms")
    public ResponseEntity<Void> createRoom(@RequestParam String roomName) {
        final Long roomId = roomService.save(roomName);
        gameService.create(roomId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/games/" + roomId));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/room")
    @ResponseBody
    public ResponseEntity<Void> deleteRoom(Long roomId) {
        roomService.delete(roomId);
        gameService.delete(roomId);
        return ResponseEntity.noContent().build();
    }
}