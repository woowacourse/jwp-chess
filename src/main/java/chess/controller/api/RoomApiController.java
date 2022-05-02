package chess.controller.api;

import chess.domain.entity.Game;
import chess.dto.request.web.CreateRoomRequest;
import chess.dto.response.web.RoomsResponse;
import chess.service.ChessService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomApiController {

    private final ChessService chessService;

    @PostMapping("/{createdRoomId}")
    public void createRoom(@PathVariable String createdRoomId,
                           @RequestBody CreateRoomRequest createRoomRequest) {
        chessService.mergeGame(
                createdRoomId,
                createRoomRequest.getRoomName(),
                createRoomRequest.getEncryptedRoomPassword(),
                createRoomRequest.getPieces(),
                "White Team",
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("")
    public RoomsResponse list() {
        List<Game> games = chessService.findAll();
        RoomsResponse roomsResponse = new RoomsResponse(games);
        System.out.println("roomsResponse = " + roomsResponse);
        return roomsResponse;
    }
}
