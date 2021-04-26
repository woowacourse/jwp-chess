package chess.chesscontroller;

import chess.domain.piece.Color;
import chess.domain.position.Position;
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
        return new RoomsResponseDto(roomService.getRooms());
    }

    @PostMapping(value = "/rooms/{roomId}")
    public PiecesResponseDto postRoom(@PathVariable("roomId") int roomId) {
        return new PiecesResponseDto(roomService.postRoom(roomId));
    }

    @PutMapping(value = "/rooms/{roomId}/pieces")
    public PiecesResponseDto putPieces(@PathVariable("roomId") int roomId,
        @RequestBody PiecesRequestDto piecesRequestDto) {
        Position source = new Position(piecesRequestDto.getSource());
        Position target = new Position(piecesRequestDto.getTarget());

        return new PiecesResponseDto(roomService.putPieces(roomId, source, target));
    }

    @GetMapping(value = "/rooms/{roomId}/score")
    public ScoreResponseDto getScore(@PathVariable int roomId, @RequestParam("color") String color) {
        return new ScoreResponseDto(roomService.getScore(roomId, Color.valueOf(color)));
    }

}
