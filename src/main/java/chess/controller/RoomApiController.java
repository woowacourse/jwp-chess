package chess.controller;

import chess.dto.request.PiecesRequestDto;
import chess.dto.response.PiecesResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.dto.response.ScoreResponseDto;
import chess.service.RoomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chess")
public class RoomApiController {

    private final RoomService roomService;

    public RoomApiController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(value = "/rooms")
    public RoomsResponseDto getRooms() {
        return roomService.getRooms();
    }

    @PostMapping(value = "/rooms/{roomId}")
    public PiecesResponseDto postRoom(@PathVariable("roomId") int roomId) {
        return roomService.postRoom(roomId);
    }

    @PutMapping(value = "/rooms/{roomId}/pieces")
    public PiecesResponseDto putPieces(@PathVariable("roomId") int roomId,
        @RequestBody PiecesRequestDto piecesRequestDto) {
        return roomService.putPieces(roomId, piecesRequestDto);
    }

    @GetMapping(value = "/rooms/{roomId}/score")
    public ScoreResponseDto getScore(@PathVariable int roomId,
        @RequestParam("color") String color) {
        return roomService.getScore(roomId, color);
    }

}
