package chess.controller.api;

import chess.dto.request.web.CreateRoomRequest;
import chess.service.ChessService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
