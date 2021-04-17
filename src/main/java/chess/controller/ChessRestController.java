package chess.controller;

import chess.domain.command.Commands;
import chess.dto.MoveRequestDto;
import chess.dto.NameDto;
import chess.domain.response.GameResponse;
import chess.domain.response.Response;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/play")
public class ChessRestController {

    private final ChessService chessService;

    public ChessRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    public ResponseEntity<Response> saveName(@RequestBody NameDto nameDto) {
        return ResponseEntity.ok(new Response(chessService.addHistory(nameDto.getName())));
    }

    @PostMapping("/move")
    public ResponseEntity<Response> move(@RequestBody MoveRequestDto moveRequestDto) {
        String command = makeMoveCmd(moveRequestDto.getSource(), moveRequestDto.getTarget());
        String id = moveRequestDto.getGameId();
        chessService.move(id, command, new Commands(command));
        return ResponseEntity.ok(new Response(new GameResponse(chessService.continuedGameInfo(id), id)));
    }

    private String makeMoveCmd(String source, String target) {
        return String.join(" ", "move", source, target);
    }
}
