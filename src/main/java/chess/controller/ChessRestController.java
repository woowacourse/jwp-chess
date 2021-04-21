package chess.controller;

import chess.domain.response.ChessResponse;
import chess.domain.response.ErrorResponse;
import chess.domain.response.GameResponse;
import chess.dto.MoveRequestDto;
import chess.dto.InitialGameInfoDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class ChessRestController {

    private final ChessService chessService;

    public ChessRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    public ResponseEntity<String> saveInfo(@RequestBody InitialGameInfoDto initialGameInfoDto) {
        final String roomId = chessService.addRoom(initialGameInfoDto.getName());
        chessService.addUser(roomId, initialGameInfoDto.getPassword());
        return ResponseEntity.ok(roomId);
    }

    @PostMapping("/move")
    public ResponseEntity<ChessResponse> move(@RequestBody MoveRequestDto moveRequestDto) {
        String id = moveRequestDto.getGameId();
        if (!chessService.checkRoomFull(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("흑팀 참가자가 아직 입장하지 않았습니다😞"));
        }
        String command = makeMoveCmd(moveRequestDto.getSource(), moveRequestDto.getTarget());
        chessService.move(id, command);
        return ResponseEntity.ok(new GameResponse(chessService.gameInfo(id), id));
    }

    private String makeMoveCmd(String source, String target) {
        return String.join(" ", "move", source, target);
    }
}
